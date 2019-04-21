package sk.skwig.aisinator.feature

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.common.util.setAll

abstract class BaseAdapter<T, VH : BaseViewHolder> : RecyclerView.Adapter<VH>() {

    protected val data = mutableListOf<T>()

    override fun getItemCount() = data.size

    protected abstract fun getDiffCallback(items: List<T>): DiffUtil.Callback

    open fun submitList(items: List<T>) {
        DiffUtil.calculateDiff(getDiffCallback(items)).dispatchUpdatesTo(this)
        data.setAll(items)
    }
}