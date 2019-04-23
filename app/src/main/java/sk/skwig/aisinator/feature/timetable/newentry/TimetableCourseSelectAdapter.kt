package sk.skwig.aisinator.feature.timetable.newentry

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.listing.ListingAdapter
import sk.skwig.aisinator.databinding.ItemCourseBinding
import sk.skwig.aisinator.feature.BaseViewHolder
import sk.skwig.aisinator.feature.SimpleDiffCallback
import sk.skwig.aisinator.feature.course.Course

class TimetableCourseSelectAdapter : ListingAdapter<Course,TimetableCourseSelectViewHolder>(){
    override fun getDiffCallback(items: List<Course>): DiffUtil.Callback {
        return SimpleDiffCallback(items, data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableCourseSelectViewHolder {
        return ItemCourseBinding.inflate(parent.layoutInflater, parent, false)
            .let(::TimetableCourseSelectViewHolder)
    }

    override fun onBindViewHolder(holder: TimetableCourseSelectViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tagText.text = item.tag
            nameText.text = item.name
        }
    }
}

class TimetableCourseSelectViewHolder(override val binding: ItemCourseBinding) : BaseViewHolder(binding)