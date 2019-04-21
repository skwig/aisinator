package sk.skwig.aisinator.feature

import androidx.recyclerview.widget.DiffUtil

class SimpleDiffCallback<T>(
    private val newItems: List<T>,
    private val oldItems: List<T>,
    private val itemComparator: (T, T) -> Boolean = { newItem, oldItem -> newItem == oldItem },
    private val contentComparator: (T, T) -> Boolean = { newItem, oldItem -> newItem == oldItem }
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        itemComparator(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        contentComparator(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size
}