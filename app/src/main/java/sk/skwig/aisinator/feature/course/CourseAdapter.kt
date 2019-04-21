package sk.skwig.aisinator.feature.course

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.feature.BaseAdapter
import sk.skwig.aisinator.feature.BaseViewHolder
import sk.skwig.aisinator.databinding.ItemCourseBinding
import sk.skwig.aisinator.common.util.layoutInflater

class CourseAdapter : BaseAdapter<Course, CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return ItemCourseBinding.inflate(parent.layoutInflater, parent, false)
            .let(::CourseViewHolder)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tagText.text = item.tag
            nameText.text = item.name
        }
    }

    override fun getDiffCallback(items: List<Course>): DiffUtil.Callback {
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

data class CourseViewHolder(override val binding: ItemCourseBinding) : BaseViewHolder(binding)