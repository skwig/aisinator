package sk.skwig.aisinator.common.deadline

import android.os.Bundle
import android.text.format.DateUtils
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.common.BaseAdapter
import sk.skwig.aisinator.common.R
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.databinding.ItemDeadlineBinding
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.slidereveallayout.SlideRevealAdapter
import sk.skwig.slidereveallayout.SlideRevealLayout
import sk.skwig.slidereveallayout.ViewBinderHelper


class DeadlineAdapter(
    private val onDelete: (Deadline) -> Unit
) : BaseAdapter<Deadline, DeadlineViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineViewHolder {
        return ItemDeadlineBinding.inflate(parent.layoutInflater, parent, false)
            .let(::DeadlineViewHolder)
    }

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
                }
            }

            // TODO: DisplayableDeadline, kde je toto uz rovno ako string & ma isBold (ak je odovzdanie v nejakom thresholde, napr dnes)
            val deadlineText = DateUtils.getRelativeTimeSpanString(item.deadline.toEpochMilli())

            title.text = item.name
            subtitle.text =
                    subtitle.resources.getString(R.string.deadline_subtitle_format, item.course.tag, deadlineText)

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

    override fun getDiffCallback(items: List<Deadline>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].id == items[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == items[newItemPosition]
            }

            override fun getOldListSize() = data.size

            override fun getNewListSize() = items.size
        }
    }
}


data class DeadlineViewHolder(val binding: ItemDeadlineBinding) : RecyclerView.ViewHolder(binding.root)