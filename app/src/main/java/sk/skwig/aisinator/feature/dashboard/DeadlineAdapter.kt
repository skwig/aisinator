package sk.skwig.aisinator.feature.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.databinding.ItemDeadlineBinding
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import sk.skwig.aisinator.util.layoutInflater
import sk.skwig.aisinator.util.setAll
import com.chauthai.swipereveallayout.ViewBinderHelper
import android.os.Bundle
import android.util.Log
import com.chauthai.swipereveallayout.SwipeRevealLayout
import sk.skwig.aisinator.MySwipeRevealLayout
import sk.skwig.aisinator.MyViewBinderHelper


class DeadlineAdapter : RecyclerView.Adapter<DeadlineViewHolder>() {

    private val viewBinderHelper = MyViewBinderHelper()

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
        viewBinderHelper.bind(holder.binding.swipeRevealLayout, item.id.toString());
        holder.binding.apply {
            holder.binding.card.setOnLongClickListener {
                (it.parent as MySwipeRevealLayout).open(true)
                false
            }
            holder.binding.swipeRevealLayout.swipeListener = object : MySwipeRevealLayout.SwipeListener {
                override fun onClosed(view: MySwipeRevealLayout) {
                    Log.d("matej", "onClosed() called with: view = [$view]")
                }

                override fun onOpened(view: MySwipeRevealLayout) {
                    Log.d("matej", "onOpened() called with: view = [$view]")
                }

                override fun onSlide(view: MySwipeRevealLayout, slideOffset: Float) {
                    Log.d("matej", "onSlide() called with: view = [$view], slideOffset = [$slideOffset]")
                }

                override fun onDismissed(view: MySwipeRevealLayout) {
                    Log.d("matej", "onDismissed() called with: view = [$view]")
                }
            }
            tagText.text = item.course.tag
            nameText.text = item.name
            deadlineText.text = item.deadline
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