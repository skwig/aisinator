package sk.skwig.aisinator.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val verticalMarginPx: Float, private val horizontalMarginPx: Float) :
    RecyclerView.ItemDecoration() {
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