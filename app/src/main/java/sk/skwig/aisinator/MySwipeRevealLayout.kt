package sk.skwig.aisinator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
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

    var isDragLocked = false
        private set

    var minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY

    var dragEdge = DRAG_EDGE_LEFT

    var swipeListener: SwipeListener? = null

    private lateinit var mainView: View
    private lateinit var revealedView: View
    private lateinit var backgroundView: View

    private var state = STATE_CLOSE
    private var mode = MODE_NORMAL

    private val mainOpenRect = Rect()
    private val mainClosedRect = Rect()

    private val revealedOpenRect = Rect()
    private val revealedClosedRect = Rect()

    private var minDistRequestDisallowParent = 0

    private var isOpenBeforeInit = false
    private var isAborted = false
    private var isScrolling = false

    private var previousMainViewLeft = 0
    private var previousMainViewTop = 0

    private var dragDistance = 0f
    private var previousX = -1f
    private var previousY = -1f

    private lateinit var dragHelper: ViewDragHelper
    private lateinit var gestureDetector: GestureDetectorCompat

    internal lateinit var dragStateChangeListener: DragStateChangeListener

    private var onLayoutCount = 0

    /**
     * @return true if layout is fully opened, false otherwise.
     */
    val isOpened: Boolean
        get() = state == STATE_OPEN

    /**
     * @return true if layout is fully closed, false otherwise.
     */
    val isClosed: Boolean
        get() = state == STATE_CLOSE

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            isScrolling = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            isScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            isScrolling = true
            parent.requestDisallowInterceptTouchEvent(true)
            return false
        }
    }

    private val openPivot: Int
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
            isAborted = false

            if (isDragLocked)
                return false

            dragHelper.captureChildView(mainView, pointerId)
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

            val velocityRightExceeded = pxToDp(xvel.toInt()) >= minFlingVelocity
            val velocityLeftExceeded = pxToDp(xvel.toInt()) <= -minFlingVelocity

            when (dragEdge) {
                DRAG_EDGE_RIGHT ->
                    when {
                        velocityRightExceeded -> close()
                        velocityLeftExceeded -> open()
                        else -> if (mainView.right < openPivot) open() else close()
                    }
                DRAG_EDGE_LEFT ->
                    when {
                        velocityRightExceeded -> open()
                        velocityLeftExceeded -> close()
                        else -> if (mainView.left < openPivot) close() else open()
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
                dragHelper.captureChildView(mainView, pointerId)
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)

            if (mode == MODE_SAME_LEVEL) {
                revealedView.offsetLeftAndRight(dx)
            }

            val isMoved = mainView.left != previousMainViewLeft || mainView.top != previousMainViewTop

            if (isMoved) {
                swipeListener?.let {
                    if (mainView.left == mainClosedRect.left && mainView.top == mainClosedRect.top) {
                        it.onClosed(this@MySwipeRevealLayout)
                    } else if (mainView.left == mainOpenRect.left && mainView.top == mainOpenRect.top) {
                        it.onOpened(this@MySwipeRevealLayout)
                    } else {
                        it.onSlide(this@MySwipeRevealLayout, slideOffset)
                    }
                }
            }

            previousMainViewLeft = mainView.left
            previousMainViewTop = mainView.top
            ViewCompat.postInvalidateOnAnimation(this@MySwipeRevealLayout)
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = this@MySwipeRevealLayout.state

            when (state) {
                ViewDragHelper.STATE_DRAGGING -> this@MySwipeRevealLayout.state = STATE_DRAGGING

                ViewDragHelper.STATE_IDLE -> {
                    Log.d(
                        "matej",
                        "onViewDragStateChanged() called with: state = [actual=${mainView.left}] closed=[${mainClosedRect.left}]"
                    )

                    // drag edge is left or right

                    if (mainView.left == mainClosedRect.left) {
                        this@MySwipeRevealLayout.state = STATE_CLOSE
                    } else {
                        this@MySwipeRevealLayout.state = STATE_OPEN
                    }
                }
            }

            if (!isAborted && prevState != this@MySwipeRevealLayout.state) {
                dragStateChangeListener.onDragStateChanged(this@MySwipeRevealLayout.state)
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
        gestureDetector.onTouchEvent(event)
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isDragLocked) {
            return super.onInterceptTouchEvent(ev)
        }

        dragHelper.processTouchEvent(ev)
        gestureDetector.onTouchEvent(ev)
        accumulateDragDist(ev)

        val couldBecomeClick = couldBecomeClick(ev)
        val settling = dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = dragHelper.viewDragState == ViewDragHelper.STATE_IDLE && isScrolling

        // must be placed as the last statement
        previousX = ev.x
        previousY = ev.y

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
        isAborted = false

        super.onLayout(changed, left, top, right, bottom)

        // taking account offset when mode is SAME_LEVEL
        if (mode == MODE_SAME_LEVEL) {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> revealedView.offsetLeftAndRight(-revealedView.width)
                DRAG_EDGE_RIGHT -> revealedView.offsetLeftAndRight(revealedView.width)
            }
        }

        initRects()

        if (isOpenBeforeInit) {
            open(false)
        } else {
            close(false)
        }

        previousMainViewLeft = mainView.left
        previousMainViewTop = mainView.top

        onLayoutCount++
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Open the panel to show the secondary view
     * @param animate true to animate the open motion. [com.chauthai.swipereveallayout.SwipeRevealLayout.SwipeListener] won't be
     * called if is animate is false.
     */
    fun open(animate: Boolean = true) {
        isOpenBeforeInit = true
        isAborted = false

        if (animate) {
            state = STATE_OPENING
            dragHelper.smoothSlideViewTo(mainView, mainOpenRect.left, mainOpenRect.top)
            dragStateChangeListener.onDragStateChanged(state)
        } else {
            state = STATE_OPEN
            dragHelper.abort()

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
     * @param animate true to animate the close motion. [com.chauthai.swipereveallayout.SwipeRevealLayout.SwipeListener] won't be
     * called if is animate is false.
     */
    fun close(animate: Boolean = true) {
        isOpenBeforeInit = false
        isAborted = false

        if (animate) {
            state = STATE_CLOSING
            dragHelper.smoothSlideViewTo(mainView, mainClosedRect.left, mainClosedRect.top)

            dragStateChangeListener.onDragStateChanged(state)

        } else {
            state = STATE_CLOSE
            dragHelper.abort()

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

    /**
     * @param lock if set to true, the user cannot drag/swipe the layout.
     */
    fun setLockDrag(lock: Boolean) {
        isDragLocked = lock
    }

    /** Abort current motion in progress. Only used for [ViewBinderHelper]  */
    fun abort() {
        isAborted = true
        dragHelper.abort()
    }

    /**
     * In RecyclerView/ListView, onLayout should be called 2 times to display children views correctly.
     * This method check if it've already called onLayout two times.
     * @return true if you should call [.requestLayout].
     */
    fun shouldRequestLayout(): Boolean {
        // TODO: dat tu 3?
        return onLayoutCount < 2
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

        val b = if (mode == MODE_NORMAL) {
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
        val minDistToInitiateDrag = dragHelper.touchSlop.toFloat()
        return dragDistance >= minDistToInitiateDrag
    }

    private fun accumulateDragDist(ev: MotionEvent) {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            dragDistance = 0f
            return
        }

        dragDistance += Math.abs(ev.x - previousX)
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
            mode = a.getInteger(R.styleable.SwipeRevealLayout_mode, MODE_NORMAL)

            minDistRequestDisallowParent = a.getDimensionPixelSize(
                R.styleable.SwipeRevealLayout_minDistRequestDisallowParent,
                dpToPx(DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT)
            )
        }

        dragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        gestureDetector = GestureDetectorCompat(context, mGestureListener)
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
