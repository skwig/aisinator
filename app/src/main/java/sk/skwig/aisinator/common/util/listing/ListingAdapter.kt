package sk.skwig.aisinator.common.util.listing

import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.feature.BaseAdapter
import sk.skwig.aisinator.feature.BaseViewHolder

abstract class ListingAdapter<T, VH : BaseViewHolder> : BaseAdapter<T, VH>()

abstract class DismissableListingAdapter<T, VH : BaseViewHolder>(
    private val onDelete: (T) -> Unit
) : ListingAdapter<T, VH>() {

    open fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onDelete(data[viewHolder.adapterPosition])
    }
}