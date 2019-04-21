package sk.skwig.aisinator.common.util

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration : RecyclerView.ItemDecoration {

    private val verticalMarginPx: Float
    private val horizontalMarginPx: Float

    constructor(verticalMarginDp: Float, horizontalMarginDp: Float) {
        Resources.getSystem().displayMetrics.also {
            verticalMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, verticalMarginDp, it)
            horizontalMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalMarginDp, it)
        }
    }

    constructor(@DimenRes verticalMarginDimenResId: Int, @DimenRes horizontalMarginDimenResId: Int) {
        Resources.getSystem().also {
            verticalMarginPx = it.getDimension(verticalMarginDimenResId)
            horizontalMarginPx = it.getDimension(horizontalMarginDimenResId)
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = verticalMarginPx.toInt()
            }
            left = horizontalMarginPx.toInt()
            right = horizontalMarginPx.toInt()
            bottom = verticalMarginPx.toInt()
        }
    }
}