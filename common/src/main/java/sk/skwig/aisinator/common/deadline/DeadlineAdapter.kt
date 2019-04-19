package sk.skwig.aisinator.common.deadline

import android.text.format.DateUtils
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.common.BaseViewHolder
import sk.skwig.aisinator.common.R
import sk.skwig.aisinator.common.SimpleDiffCallback
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.databinding.ItemDeadlineBinding
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.listing.DismissableListingAdapter


class DeadlineAdapter(onDelete: (Deadline) -> Unit) :
    DismissableListingAdapter<Deadline, DeadlineViewHolder>(onDelete) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineViewHolder {
        return ItemDeadlineBinding.inflate(parent.layoutInflater, parent, false)
            .let(::DeadlineViewHolder)
    }

    override fun onBindViewHolder(holder: DeadlineViewHolder, position: Int) {

        holder.binding.apply {
            val item = data[position]

            // TODO: DisplayableDeadline, kde je toto uz rovno ako string & ma isBold (ak je odovzdanie v nejakom thresholde, napr dnes)
            val deadlineText = DateUtils.getRelativeTimeSpanString(item.deadline.toEpochMilli())

            title.text = item.name
            subtitle.text =
                subtitle.resources.getString(R.string.deadline_subtitle_format, item.course.tag, deadlineText)
        }
    }

    override fun getDiffCallback(items: List<Deadline>): DiffUtil.Callback {
        return SimpleDiffCallback(items, data, { new, old -> new.id == old.id })
    }
}


data class DeadlineViewHolder(override val binding: ItemDeadlineBinding) : BaseViewHolder(binding)