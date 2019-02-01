package sk.skwig.aisinator.feature.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.databinding.ItemDeadlineBinding
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import sk.skwig.aisinator.util.layoutInflater
import sk.skwig.aisinator.util.setAll

class DeadlineAdapter : RecyclerView.Adapter<DeadlineViewHolder>() {

    private val data = mutableListOf<CourseworkDeadline>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineViewHolder {
        return ItemDeadlineBinding.inflate(parent.layoutInflater, parent, false)
            .let(::DeadlineViewHolder)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DeadlineViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tagText.text = item.course.tag
            nameText.text = item.name
        }
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