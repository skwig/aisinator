package sk.skwig.aisinator.dashboard

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.databinding.ItemDeadlineBinding
import sk.skwig.aisinator.course.CourseworkDeadline
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.setAll
import sk.skwig.slidereveallayout.SlideRevealAdapter
import sk.skwig.slidereveallayout.SlideRevealLayout
import sk.skwig.slidereveallayout.ViewBinderHelper


class DeadlineAdapter(
    private val onDelete: (CourseworkDeadline) -> Unit
) : RecyclerView.Adapter<DeadlineViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    private val data = mutableListOf<CourseworkDeadline>()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineViewHolder {
        return ItemDeadlineBinding.inflate(parent.layoutInflater, parent, false)
            .let(::DeadlineViewHolder)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DeadlineViewHolder, position: Int) {
        val item = data[position]
        viewBinderHelper.bind(holder.binding.slideRevealLayout, item.id.toString());
        holder.binding.apply {
            holder.binding.card.setOnLongClickListener {
                (it.parent as SlideRevealLayout).open(true)
                false
            }
            holder.binding.slideRevealLayout.listener = object : SlideRevealAdapter() {
                override fun onSwipe(view: SlideRevealLayout) {
                    val adapterPosition = holder.adapterPosition

                    if (adapterPosition == RecyclerView.NO_POSITION) {
                        return
                    }

                    onDelete(data[adapterPosition])

//                    data.toMutableList()
//                        .apply { removeAt(adapterPosition) }
//                        .let(::submitList)
                }
            }
            tagText.text = item.course.tag
            nameText.text = item.name
            deadlineText.text = item.deadline

            holder.binding.delete.setOnClickListener {
                holder.binding.slideRevealLayout.swipe()
            }
        }
    }

    fun saveStates(outState: Bundle) {
        viewBinderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle) {
        viewBinderHelper.restoreStates(inState)
    }

    fun submitList(courses: List<CourseworkDeadline>) {
        DiffUtil.calculateDiff(diffCallback(courses)).dispatchUpdatesTo(this)
        data.setAll(courses)
    }

    private fun diffCallback(courses: List<CourseworkDeadline>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].id == courses[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == courses[newItemPosition]
            }

            override fun getOldListSize() = data.size

            override fun getNewListSize() = courses.size
        }
    }
}


data class DeadlineViewHolder(val binding: ItemDeadlineBinding) : RecyclerView.ViewHolder(binding.root)