package sk.skwig.aisinator.feature.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.databinding.ItemCourseBinding
import sk.skwig.aisinator.course.Course
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.setAll

class CourseAdapter : RecyclerView.Adapter<CourseViewHolder>() {

    private val data = mutableListOf<Course>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return ItemCourseBinding.inflate(parent.layoutInflater, parent, false)
            .let(::CourseViewHolder)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tagText.text = item.tag
            nameText.text = item.name
        }
    }

    fun submitList(courses: List<Course>) {
        DiffUtil.calculateDiff(diffCallback(courses)).dispatchUpdatesTo(this)
        data.setAll(courses)
    }

    private fun diffCallback(courses: List<Course>): DiffUtil.Callback {
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


data class CourseViewHolder(val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root)