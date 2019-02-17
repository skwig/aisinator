package sk.skwig.aisinator.timetable

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.common.BaseAdapter
import sk.skwig.aisinator.common.BaseViewHolder
import sk.skwig.aisinator.common.SimpleDiffCallback
import sk.skwig.aisinator.common.data.Lesson
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.timetable.databinding.ItemTimetableLessonBinding

class TimetableAdapter : BaseAdapter<Lesson, TimetableLessonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableLessonViewHolder {
        return ItemTimetableLessonBinding.inflate(parent.layoutInflater, parent, false)
            .let(::TimetableLessonViewHolder)
    }

    override fun onBindViewHolder(holderTimetable: TimetableLessonViewHolder, position: Int) {
        val item = data[position]
        holderTimetable.binding.apply {
        }
    }

    override fun getDiffCallback(items: List<Lesson>): DiffUtil.Callback =
        SimpleDiffCallback(items, data, itemComparator = { new, old -> new == old })
}

data class TimetableLessonViewHolder(override val binding: ItemTimetableLessonBinding) : BaseViewHolder(binding)