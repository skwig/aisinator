package sk.skwig.aisinator.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.setAll

class UpcomingLessonsAdapter : RecyclerView.Adapter<UpcomingLessonViewHolder>() {

    private val data = mutableListOf<UpcomingLesson>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingLessonViewHolder {
        return sk.skwig.aisinator.dashboard.databinding.ItemUpcomingLessonBinding.inflate(parent.layoutInflater, parent, false)
            .let(::UpcomingLessonViewHolder)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UpcomingLessonViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
//            tagText.text = item.tag
//            nameText.text = item.name
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


data class UpcomingLessonViewHolder(val binding: sk.skwig.aisinator.dashboard.databinding.ItemUpcomingLessonBinding) : RecyclerView.ViewHolder(binding.root)