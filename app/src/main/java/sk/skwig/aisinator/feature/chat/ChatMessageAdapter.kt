package sk.skwig.aisinator.feature.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.listing.ListingAdapter
import sk.skwig.aisinator.databinding.ItemChatmessageBinding
import sk.skwig.aisinator.databinding.ItemChatroomBinding
import sk.skwig.aisinator.feature.BaseViewHolder
import sk.skwig.aisinator.feature.SimpleDiffCallback

class ChatMessageAdapter : ListingAdapter<ChatMessage, ChatMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ItemChatmessageBinding.inflate(parent.layoutInflater, parent, false)
            .let(::ChatMessageViewHolder)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            author.text = "Dano"
            text.text = item.text
        }
    }

    override fun getDiffCallback(items: List<ChatMessage>): DiffUtil.Callback {
        return SimpleDiffCallback(items, data)
    }
}

data class ChatMessageViewHolder(override val binding: ItemChatmessageBinding) : BaseViewHolder(binding)