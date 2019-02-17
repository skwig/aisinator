package sk.skwig.aisinator.common.lesson

import android.text.format.DateUtils
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.common.R
import sk.skwig.aisinator.common.data.UpcomingLesson
import sk.skwig.aisinator.common.databinding.ItemUpcomingLessonBinding
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.setAll
import java.time.Instant

class UpcomingLessonsAdapter : RecyclerView.Adapter<UpcomingLessonViewHolder>() {

    private val data = mutableListOf<UpcomingLesson>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingLessonViewHolder {
        return ItemUpcomingLessonBinding.inflate(parent.layoutInflater, parent, false)
            .let(::UpcomingLessonViewHolder)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UpcomingLessonViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            // TODO: DisplayableUpcomingLesson, kde je toto uz rovno ako string & ma isBold (ak je zaciatok/koniec v nejakom thresholde)

            val time: Instant
            @StringRes val startOrEndText: Int
            if (Instant.now().isBefore(item.startTime)) {
                time = item.startTime
                startOrEndText = R.string.starts_in
            } else if (Instant.now().isBefore(item.endTime)) {
                time = item.endTime
                startOrEndText = R.string.ends_in
            } else {
                time = item.endTime
                startOrEndText = R.string.ended
            }

            title.text = item.lesson.course.name
            subtitle.text = subtitle.resources.getString(
                R.string.upcoming_lesson_subtitle_format,
                item.lesson.course.tag,
                item.lesson.room,
                subtitle.resources.getString(startOrEndText),
                DateUtils.getRelativeTimeSpanString(subtitle.context, time.toEpochMilli(), true)
            )
        }
    }

    fun submitList(upcomingLessons: List<UpcomingLesson>) {
        DiffUtil.calculateDiff(diffCallback(upcomingLessons)).dispatchUpdatesTo(this)
        data.setAll(upcomingLessons)
    }

    private fun diffCallback(upcomingLessons: List<UpcomingLesson>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == upcomingLessons[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == upcomingLessons[newItemPosition]
            }

            override fun getOldListSize() = data.size

            override fun getNewListSize() = upcomingLessons.size
        }
    }
}


data class UpcomingLessonViewHolder(val binding: ItemUpcomingLessonBinding) :
    RecyclerView.ViewHolder(binding.root)