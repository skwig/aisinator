package sk.skwig.aisinator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.chauthai.swipereveallayout.ViewBinderHelper

// TODO: pridat dalsie stavy? nech open ked je za openom (ide swipe prec) "nezatvara" (dava poziciu na open, cize "zavrie") - aj dist to closest edge
// TODO: detekovat swipe? -> pridat aj min dismiss velocity

@SuppressLint("RtlHardcoded")
class MySwipeRevealLayout : FrameLayout {

    companion object {
        // These states are used only for ViewBindHelper
        val STATE_CLOSE = 0
        val STATE_CLOSING = 1
        val STATE_OPEN = 2
        val STATE_OPENING = 3
        val STATE_DRAGGING = 4

        private val DEFAULT_MIN_FLING_VELOCITY = 300 // dp per second
        private val DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1 // dp

        val DRAG_EDGE_LEFT = 0x1
        val DRAG_EDGE_RIGHT = 0x1 shl 1

        /**
         * The secondary view will be under the main view.
         */
        val MODE_NORMAL = 0

        /**
         * The secondary view will stick the edge of the main view.
         */
        val MODE_SAME_LEVEL = 1
    }

    private lateinit var mainView: View
    private lateinit var revealedView: View
    private lateinit var backgroundView: View

    private val mainClosedRect = Rect()
    private val mainOpenRect = Rect()

    private val revealedClosedRect = Rect()
    private val revealedOpenRect = Rect()

    /**
     * The minimum distance (px) to the closest drag edge that the SwipeRevealLayout
     * will disallow the parent to intercept touch event.
     */
    private var mMinDistRequestDisallowParent = 0

    private var mIsOpenBeforeInit = false
    @Volatile
    private var mAborted = false
    @Volatile
    private var mIsScrolling = false
    /**
     * @return true if the drag/swipe motion is currently locked.
     */
    @Volatile
    var isDragLocked = false
        private set

    /**
     * Get the minimum fling velocity to cause the layout to open/close.
     * @return dp per second
     */
    /**
     * Set the minimum fling velocity to cause the layout to open/close.
     * @param velocity dp per second
     */
    var minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
    private var mState = STATE_CLOSE
    private var mMode = MODE_NORMAL

    private var mLastMainLeft = 0
    private var mLastMainTop = 0

    /**
     * Get the edge where the layout can be dragged from.
     * @return Can be one of these
     *
     *  * [.DRAG_EDGE_LEFT]
     *  * [.DRAG_EDGE_TOP]
     *  * [.DRAG_EDGE_RIGHT]
     *  * [.DRAG_EDGE_BOTTOM]
     *
     */
    /**
     * Set the edge where the layout can be dragged from.
     * @param dragEdge Can be one of these
     *
     *  * [.DRAG_EDGE_LEFT]
     *  * [.DRAG_EDGE_TOP]
     *  * [.DRAG_EDGE_RIGHT]
     *  * [.DRAG_EDGE_BOTTOM]
     *
     */
    var dragEdge = DRAG_EDGE_LEFT

    private var mDragDist = 0f
    private var mPrevX = -1f
    private var mPrevY = -1f

    private lateinit var mDragHelper: ViewDragHelper
    private lateinit var mGestureDetector: GestureDetectorCompat

    private var mDragStateChangeListener: DragStateChangeListener? = null // only used for ViewBindHelper
    private var mSwipeListener: SwipeListener? = null

    private var mOnLayoutCount = 0

    /**
     * @return true if layout is fully opened, false otherwise.
     */
    val isOpened: Boolean
        get() = mState == STATE_OPEN

    /**
     * @return true if layout is fully closed, false otherwise.
     */
    val isClosed: Boolean
        get() = mState == STATE_CLOSE

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            mIsScrolling = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            mIsScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mIsScrolling = true
            parent.requestDisallowInterceptTouchEvent(true)
            return false
        }
    }

    private val halfwayPivotHorizontal: Int
        get() = if (dragEdge == DRAG_EDGE_LEFT) {
            mainClosedRect.left + revealedView.width / 2
        } else {
            mainClosedRect.right - revealedView.width / 2
        }

    private val halfwayPivotVertical: Int
        get() = mainClosedRect.bottom - revealedView.height / 2

    private val mDragHelperCallback = object : ViewDragHelper.Callback() {

        private val slideOffset: Float
            get() = when (dragEdge) {
                DRAG_EDGE_LEFT -> (mainView.left - mainClosedRect.left).toFloat() / revealedView.width
                DRAG_EDGE_RIGHT -> (mainClosedRect.left - mainView.left).toFloat() / revealedView.width
                else -> 0f
            }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            mAborted = false

            if (isDragLocked)
                return false

            mDragHelper.captureChildView(mainView, pointerId)
            return false
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return child.top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_RIGHT -> Math.min(left, mainClosedRect.left)
                DRAG_EDGE_LEFT -> Math.max(
                    Math.min(left, mainClosedRect.left + revealedView.width),
                    mainClosedRect.left
                )
                else -> child.left
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val velRightExceeded = pxToDp(xvel.toInt()) >= minFlingVelocity
            val velLeftExceeded = pxToDp(xvel.toInt()) <= -minFlingVelocity
            val velUpExceeded = pxToDp(yvel.toInt()) <= -minFlingVelocity
            val velDownExceeded = pxToDp(yvel.toInt()) >= minFlingVelocity

            val pivotHorizontal = halfwayPivotHorizontal
            val pivotVertical = halfwayPivotVertical

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> if (velRightExceeded) {
                    close(true)
                } else if (velLeftExceeded) {
                    open(true)
                } else {
                    if (mainView.right < pivotHorizontal) {
                        open(true)
                    } else {
                        close(true)
                    }
                }

                DRAG_EDGE_LEFT -> if (velRightExceeded) {
                    open(true)
                } else if (velLeftExceeded) {
                    close(true)
                } else {
                    if (mainView.left < pivotHorizontal) {
                        close(true)
                    } else {
                        open(true)
                    }
                }
            }
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)

            if (isDragLocked) {
                return
            }

            val edgeStartLeft = dragEdge == DRAG_EDGE_RIGHT && edgeFlags == ViewDragHelper.EDGE_LEFT
            val edgeStartRight = dragEdge == DRAG_EDGE_LEFT && edgeFlags == ViewDragHelper.EDGE_RIGHT

            if (edgeStartLeft || edgeStartRight) {
                mDragHelper.captureChildView(mainView, pointerId)
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (mMode == MODE_SAME_LEVEL) {
                revealedView.offsetLeftAndRight(dx)
            }

            val isMoved = mainView.left != mLastMainLeft || mainView.top != mLastMainTop
            if (mSwipeListener != null && isMoved) {
                if (mainView.left == mainClosedRect.left && mainView.top == mainClosedRect.top) {
                    mSwipeListener!!.onClosed(this@MySwipeRevealLayout)
                } else if (mainView.left == mainOpenRect.left && mainView.top == mainOpenRect.top) {
                    mSwipeListener!!.onOpened(this@MySwipeRevealLayout)
                } else {
                    mSwipeListener!!.onSlide(this@MySwipeRevealLayout, slideOffset)
                }
            }

            mLastMainLeft = mainView.left
            mLastMainTop = mainView.top
            ViewCompat.postInvalidateOnAnimation(this@MySwipeRevealLayout)
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = mState

            when (state) {
                ViewDragHelper.STATE_DRAGGING -> mState = STATE_DRAGGING

                ViewDragHelper.STATE_IDLE ->

                    // drag edge is left or right
                    if (mainView.left == mainClosedRect.left) {
                        mState = STATE_CLOSE
                    } else {
                        mState = STATE_OPEN
                    }
            }

            if (mDragStateChangeListener != null && !mAborted && prevState != mState) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }
        }
    }

    internal interface DragStateChangeListener {
        fun onDragStateChanged(state: Int)
    }

    interface SwipeListener {
        fun onClosed(view: MySwipeRevealLayout)

        fun onOpened(view: MySwipeRevealLayout)

        fun onSlide(view: MySwipeRevealLayout, slideOffset: Float)
    }

    class SimpleSwipeListener : SwipeListener {
        override fun onClosed(view: MySwipeRevealLayout) {}

        override fun onOpened(view: MySwipeRevealLayout) {}

        override fun onSlide(view: MySwipeRevealLayout, slideOffset: Float) {}
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        mDragHelper.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isDragLocked) {
            return super.onInterceptTouchEvent(ev)
        }

        mDragHelper!!.processTouchEvent(ev)
        mGestureDetector!!.onTouchEvent(ev)
        accumulateDragDist(ev)

        val couldBecomeClick = couldBecomeClick(ev)
        val settling = mDragHelper.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = mDragHelper.viewDragState == ViewDragHelper.STATE_IDLE && mIsScrolling

        // must be placed as the last statement
        mPrevX = ev.x
        mPrevY = ev.y

        // return true => intercept, cannot trigger onClick event
        return !couldBecomeClick && (settling || idleAfterScrolled)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        when (val i = childCount) {
            3 -> {
                backgroundView = getChildAt(0)
                revealedView = getChildAt(1)
                mainView = getChildAt(2)
            }
            else -> throw IllegalStateException("Invalid MySwipeRevealLayout child count : $i")
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mAborted = false

        super.onLayout(changed, left, top, right, bottom)

        // taking account offset when mode is SAME_LEVEL
        if (mMode == MODE_SAME_LEVEL) {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> revealedView.offsetLeftAndRight(-revealedView.width)
                DRAG_EDGE_RIGHT -> revealedView.offsetLeftAndRight(revealedView.width)
            }
        }

        initRects()

        if (mIsOpenBeforeInit) {
            open(false)
        } else {
            close(false)
        }

        mLastMainLeft = mainView.left
        mLastMainTop = mainView.top

        mOnLayoutCount++
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Open the panel to show the secondary view
     * @param animation true to animate the open motion. [com.chauthai.swipereveallayout.SwipeRevealLayout.SwipeListener] won't be
     * called if is animation is false.
     */
    fun open(animation: Boolean) {
        mIsOpenBeforeInit = true
        mAborted = false

        if (animation) {
            mState = STATE_OPENING
            mDragHelper.smoothSlideViewTo(mainView, mainOpenRect.left, mainOpenRect.top)

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }
        } else {
            mState = STATE_OPEN
            mDragHelper.abort()

            mainView.layout(
                mainOpenRect.left,
                mainOpenRect.top,
                mainOpenRect.right,
                mainOpenRect.bottom
            )

            revealedView.layout(
                revealedOpenRect.left,
                revealedOpenRect.top,
                revealedOpenRect.right,
                revealedOpenRect.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this@MySwipeRevealLayout)
    }

    /**
     * Close the panel to hide the secondary view
     * @param animation true to animate the close motion. [com.chauthai.swipereveallayout.SwipeRevealLayout.SwipeListener] won't be
     * called if is animation is false.
     */
    fun close(animation: Boolean) {
        mIsOpenBeforeInit = false
        mAborted = false

        if (animation) {
            mState = STATE_CLOSING
            mDragHelper.smoothSlideViewTo(mainView, mainClosedRect.left, mainClosedRect.top)

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }

        } else {
            mState = STATE_CLOSE
            mDragHelper.abort()

            mainView.layout(
                mainClosedRect.left,
                mainClosedRect.top,
                mainClosedRect.right,
                mainClosedRect.bottom
            )

            revealedView.layout(
                revealedClosedRect.left,
                revealedClosedRect.top,
                revealedClosedRect.right,
                revealedClosedRect.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this@MySwipeRevealLayout)
    }

    fun setSwipeListener(listener: SwipeListener) {
        mSwipeListener = listener
    }

    /**
     * @param lock if set to true, the user cannot drag/swipe the layout.
     */
    fun setLockDrag(lock: Boolean) {
        isDragLocked = lock
    }

    /** Only used for [ViewBinderHelper]  */
    internal fun setDragStateChangeListener(listener: DragStateChangeListener) {
        mDragStateChangeListener = listener
    }

    /** Abort current motion in progress. Only used for [ViewBinderHelper]  */
    fun abort() {
        mAborted = true
        mDragHelper.abort()
    }

    /**
     * In RecyclerView/ListView, onLayout should be called 2 times to display children views correctly.
     * This method check if it've already called onLayout two times.
     * @return true if you should call [.requestLayout].
     */
    fun shouldRequestLayout(): Boolean {
        // TODO: dat tu 3?
        return mOnLayoutCount < 2
    }

    private fun initRects() {
        // close position of main view
        mainClosedRect.set(
            mainView.left,
            mainView.top,
            mainView.right,
            mainView.bottom
        )

        // close position of secondary view
        revealedClosedRect.set(
            revealedView.left,
            revealedView.top,
            revealedView.right,
            revealedView.bottom
        )

        val a = when (dragEdge) {
            DRAG_EDGE_LEFT -> mainClosedRect.left + revealedView.width
            DRAG_EDGE_RIGHT -> mainClosedRect.left - revealedView.width
            else -> 0
        }

        // open position of the main view
        mainOpenRect.set(
            a,
            mainView.top,
            a + mainView.width,
            mainView.bottom
        )

        val b = if (mMode == MODE_NORMAL) {
            revealedClosedRect.left
        } else if (dragEdge == DRAG_EDGE_LEFT) {
            revealedClosedRect.left + revealedView.width
        } else {
            revealedClosedRect.left - revealedView.width
        }

        // open position of the secondary view
        revealedOpenRect.set(
            b,
            revealedView.top,
            b + revealedView.width,
            revealedView.bottom
        )
    }

    private fun couldBecomeClick(ev: MotionEvent): Boolean {
        return isInMainView(ev) && !shouldInitiateADrag()
    }

    private fun isInMainView(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y

        val withinVertical = mainView.top <= y && y <= mainView.bottom
        val withinHorizontal = mainView.left <= x && x <= mainView.right

        return withinVertical && withinHorizontal
    }

    private fun shouldInitiateADrag(): Boolean {
        val minDistToInitiateDrag = mDragHelper.touchSlop.toFloat()
        return mDragDist >= minDistToInitiateDrag
    }

    private fun accumulateDragDist(ev: MotionEvent) {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            mDragDist = 0f
            return
        }

        mDragDist += Math.abs(ev.x - mPrevX)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null && context != null) {
            val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SwipeRevealLayout,
                0, 0
            )

            dragEdge = a.getInteger(R.styleable.SwipeRevealLayout_dragEdge, DRAG_EDGE_LEFT)
            minFlingVelocity = a.getInteger(R.styleable.SwipeRevealLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY)
            mMode = a.getInteger(R.styleable.SwipeRevealLayout_mode, MODE_NORMAL)

            mMinDistRequestDisallowParent = a.getDimensionPixelSize(
                R.styleable.SwipeRevealLayout_minDistRequestDisallowParent,
                dpToPx(DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT)
            )
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        mGestureDetector = GestureDetectorCompat(context, mGestureListener)
    }

    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

}
