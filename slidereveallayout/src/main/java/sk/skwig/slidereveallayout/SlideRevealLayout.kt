package sk.skwig.slidereveallayout

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
import androidx.annotation.IdRes
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper

private const val DEFAULT_MIN_FLING_OPEN_VELOCITY_DP = 300 // dp per second
private const val DEFAULT_MIN_FLING_DISMISS_VELOCITY_DP = 2000 // dp per second

@SuppressLint("RtlHardcoded")
class SlideRevealLayout : FrameLayout {

    companion object {
        val STATE_CLOSED = 0
        val STATE_CLOSING = 1

        val STATE_OPENED = 2
        val STATE_OPENING = 3

        val STATE_SWIPED = 4
        val STATE_SWIPING = 5

        val STATE_DRAGGING = 6

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

    internal interface DragStateChangeListener {
        fun onDragStateChanged(state: Int)
    }

    interface Listener {
        fun onClose(view: SlideRevealLayout)
        fun onOpen(view: SlideRevealLayout)
        fun onSwipe(view: SlideRevealLayout)
        fun onSlide(view: SlideRevealLayout, slideOffset: Float)
    }

    var minFlingOpenVelocity = DEFAULT_MIN_FLING_OPEN_VELOCITY_DP
    var minFlingDismissVelocity = DEFAULT_MIN_FLING_DISMISS_VELOCITY_DP

    var dragEdge = DRAG_EDGE_LEFT
    internal var isDragLocked = false

    var listener: Listener? = null

    val isOpened: Boolean
        get() = state == STATE_OPENED

    val isClosed: Boolean
        get() = state == STATE_CLOSED

    val isDismissed: Boolean
        get() = state == STATE_SWIPED

    @IdRes
    private var mainViewId : Int = 0

    @IdRes
    private var revealedViewId : Int = 0

    private lateinit var mainView: View
    private lateinit var revealedView: View

    private var state = STATE_CLOSED
    private var mode = MODE_NORMAL

    private val mainOpenRect = Rect()
    private val mainClosedRect = Rect()
    private val mainSwipedRect = Rect()

    private val revealedOpenRect = Rect()
    private val revealedClosedRect = Rect()

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

    private val openPivot: Int
        get() = if (dragEdge == DRAG_EDGE_LEFT) {
            mainClosedRect.left + revealedView.width / 2
        } else {
            mainClosedRect.right - revealedView.width / 2
        }

    private val swipePivot: Int
        get() = if (dragEdge == DRAG_EDGE_LEFT) {
            TODO()
        } else {
            mainClosedRect.left + (mainClosedRect.right - revealedView.width) / 2
        }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {

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

    private val dragHelperCallback = object : ViewDragHelper.Callback() {

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

            val dpXvel = pxToDp(xvel.toInt())

            val openVelocityExceeded = dpXvel <= -minFlingOpenVelocity
            val closeVelocityExceeded = dpXvel >= minFlingOpenVelocity
            val dismissVelocityExceeded = dpXvel <= -minFlingDismissVelocity
            val openedToDismissVelocityExceeded = dpXvel <= -(minFlingDismissVelocity - minFlingOpenVelocity)

            val closeFromPivot = mainView.right > openPivot
            val dismissFromPivot = mainView.right < swipePivot
            val openFromPivot = mainView.right in swipePivot..openPivot

            when {
                dismissVelocityExceeded -> internalSwipe(isFlinging = true)
                openVelocityExceeded -> open()
                closeVelocityExceeded -> close()
                openedToDismissVelocityExceeded && state == STATE_OPENED -> internalSwipe(isFlinging = true)
                openFromPivot -> open()
                closeFromPivot -> close()
                dismissFromPivot -> internalSwipe(isFlinging = true)
                else -> throw IllegalStateException()
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
                listener?.let {
                    if (mainView.left == mainClosedRect.left && mainView.top == mainClosedRect.top) {
                        it.onClose(this@SlideRevealLayout)
                    } else if (mainView.left == mainOpenRect.left && mainView.top == mainOpenRect.top) {
                        it.onOpen(this@SlideRevealLayout)
                    } else if (mainView.left == mainSwipedRect.left && mainView.top == mainSwipedRect.top) {
                        it.onSwipe(this@SlideRevealLayout)
                    } else {
                        it.onSlide(this@SlideRevealLayout, slideOffset)
                    }
                }
            }

            previousMainViewLeft = mainView.left
            previousMainViewTop = mainView.top
            ViewCompat.postInvalidateOnAnimation(this@SlideRevealLayout)
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = this@SlideRevealLayout.state

            when (state) {
                ViewDragHelper.STATE_DRAGGING -> this@SlideRevealLayout.state = STATE_DRAGGING

                ViewDragHelper.STATE_IDLE -> {
                    Log.d(
                        "matej",
                        "onViewDragStateChanged() called with: state = [actual=${mainView.left}] closed=[${mainClosedRect.left}]"
                    )

                    this@SlideRevealLayout.state = when (mainView.left) {
                        mainOpenRect.left -> STATE_OPENED
                        mainClosedRect.left -> STATE_CLOSED
                        mainSwipedRect.left -> STATE_SWIPED
                        else -> throw IllegalStateException()
                    }
                }
            }

            if (!isAborted && prevState != this@SlideRevealLayout.state) {
                dragStateChangeListener.onDragStateChanged(this@SlideRevealLayout.state)
            }
        }
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

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null && context != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideRevealLayout, 0, 0)

            dragEdge = a.getInteger(R.styleable.SlideRevealLayout_dragEdge, DRAG_EDGE_LEFT)
            minFlingOpenVelocity =
                    a.getInteger(R.styleable.SlideRevealLayout_flingOpenVelocity, DEFAULT_MIN_FLING_OPEN_VELOCITY_DP)
            minFlingDismissVelocity = a.getInteger(
                R.styleable.SlideRevealLayout_flingDismissVelocity,
                DEFAULT_MIN_FLING_DISMISS_VELOCITY_DP
            )

            mainViewId = a.getResourceId(R.styleable.SlideRevealLayout_slidingView, 0)
            revealedViewId = a.getResourceId(R.styleable.SlideRevealLayout_revealedView, 0)

            mode = a.getInteger(R.styleable.SlideRevealLayout_mode, MODE_NORMAL)
        }

        dragHelper = ViewDragHelper.create(this, 1.0f, dragHelperCallback)
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        gestureDetector = GestureDetectorCompat(context, gestureListener)
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

        val mainOpenRectTop = when (dragEdge) {
            DRAG_EDGE_LEFT -> mainClosedRect.left + revealedView.width
            DRAG_EDGE_RIGHT -> mainClosedRect.left - revealedView.width
            else -> 0
        }

        // open position of the main view
        mainOpenRect.set(
            mainOpenRectTop,
            mainView.top,
            mainOpenRectTop + mainView.width,
            mainView.bottom
        )

        val revealedOpenTop = if (mode == MODE_NORMAL) {
            revealedClosedRect.left
        } else if (dragEdge == DRAG_EDGE_LEFT) {
            revealedClosedRect.left + revealedView.width
        } else {
            revealedClosedRect.left - revealedView.width
        }

        // open position of the secondary view
        revealedOpenRect.set(
            revealedOpenTop,
            revealedView.top,
            revealedOpenTop + revealedView.width,
            revealedView.bottom
        )

        mainOpenRect.set(
            mainOpenRectTop,
            mainView.top,
            mainOpenRectTop + mainView.width,
            mainView.bottom
        )

        if (dragEdge == DRAG_EDGE_LEFT) {
            TODO()
        }

        // swiped position of the main view
        val mainSwipedTop = mainOpenRect.left - mainClosedRect.right
        mainSwipedRect.set(
            mainSwipedTop,
            mainView.top,
            mainSwipedTop + mainView.width,
            mainView.bottom
        )
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
            2 -> {
                mainView = findViewById(mainViewId)
                revealedView = findViewById(revealedViewId)
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
            state = STATE_OPENED
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

        ViewCompat.postInvalidateOnAnimation(this@SlideRevealLayout)
    }

    fun swipe(animate: Boolean = true) {
        internalSwipe(animate, false)
    }

    private fun internalSwipe(animate: Boolean = true, isFlinging: Boolean = false) {
        isOpenBeforeInit = true
        isAborted = false

        if (animate) {
            state = STATE_SWIPING
            if (isFlinging) {
                dragHelper.flingCapturedView(
                    mainSwipedRect.left,
                    mainSwipedRect.top,
                    mainSwipedRect.left,
                    mainSwipedRect.top
                )
            } else {
                dragHelper.smoothSlideViewTo(mainView, mainSwipedRect.left, mainSwipedRect.top)
            }
            dragStateChangeListener.onDragStateChanged(state)
        } else {
            state = STATE_SWIPED
            dragHelper.abort()

            mainView.layout(
                mainSwipedRect.left,
                mainSwipedRect.top,
                mainSwipedRect.right,
                mainSwipedRect.bottom
            )

            revealedView.layout(
                revealedOpenRect.left,
                revealedOpenRect.top,
                revealedOpenRect.right,
                revealedOpenRect.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this@SlideRevealLayout)
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
            state = STATE_CLOSED
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

        ViewCompat.postInvalidateOnAnimation(this@SlideRevealLayout)
    }

    internal fun abort() {
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

    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}
