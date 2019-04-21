package sk.skwig.aisinator.feature.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.listing.ListingAdapter
import sk.skwig.aisinator.databinding.ItemChatroomBinding
import sk.skwig.aisinator.feature.BaseViewHolder
import sk.skwig.aisinator.feature.SimpleDiffCallback

class ChatroomAdapter(private val onItemClicked: (Chatroom) -> Unit) : ListingAdapter<Chatroom, ChatroomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        return ItemChatroomBinding.inflate(parent.layoutInflater, parent, false)
            .let(::ChatroomViewHolder)
            .also { holder ->
                holder.binding.root.setOnClickListener { onItemClicked(data[holder.adapterPosition]) }
            }
    }

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tagText.text = item.course.tag
            nameText.text = item.course.name
        }
    }

    override fun getDiffCallback(items: List<Chatroom>): DiffUtil.Callback {
        return SimpleDiffCallback(items, data)
    }
}

data class ChatroomViewHolder(override val binding: ItemChatroomBinding) : BaseViewHolder(binding)