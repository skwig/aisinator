package sk.skwig.aisinator.common

import androidx.recyclerview.widget.DiffUtil

class SimpleDiffCallback<T>(
    private val newItems: List<T>,
    private val oldItems: List<T>,
    private val itemComparator: (T, T) -> Boolean,
    private val contentComparator: (T, T) -> Boolean = { newItem, oldItem -> newItem == oldItem }
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        itemComparator(newItems[oldItemPosition], oldItems[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        contentComparator(newItems[oldItemPosition], oldItems[newItemPosition])

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size
}