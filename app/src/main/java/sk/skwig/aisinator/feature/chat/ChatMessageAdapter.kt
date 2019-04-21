package sk.skwig.aisinator.feature.chat

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.skwig.aisinator.common.util.layoutInflater
import sk.skwig.aisinator.common.util.setAll
import sk.skwig.aisinator.databinding.ItemChatmessageBinding
import sk.skwig.aisinator.databinding.ItemChatmessageErrorBinding
import sk.skwig.aisinator.databinding.ItemChatmessageLoadingBinding
import sk.skwig.aisinator.feature.BaseViewHolder
import sk.skwig.aisinator.feature.SimpleDiffCallback
import sk.skwig.aisinator.feature.chat.paging.ChatPagingState

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageItemViewHolder>() {

    private val data = mutableListOf<ChatMessage>()

    private var adapterState: AdapterState = AdapterState.NORMAL
        set(value) {

            val isExtraItem = field == AdapterState.ERROR || field == AdapterState.LOADING
            val willBeExtraItem = value == AdapterState.ERROR || value == AdapterState.LOADING

            when {
                isExtraItem && willBeExtraItem && field != value -> notifyItemChanged(data.size)
                isExtraItem && !willBeExtraItem -> notifyItemRemoved(data.size)
                !isExtraItem && willBeExtraItem -> notifyItemInserted(data.size)
                !isExtraItem && !willBeExtraItem -> { /*noop*/ }
            }

            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageItemViewHolder {
        return when (viewType) {
            ViewType.NORMAL.ordinal -> ItemChatmessageBinding.inflate(parent.layoutInflater, parent, false)
                .let(::ChatMessageViewHolder)
            ViewType.LOADING.ordinal -> ItemChatmessageLoadingBinding.inflate(parent.layoutInflater, parent, false)
                .let(::ChatLoadingViewHolder)
            ViewType.ERROR.ordinal -> ItemChatmessageErrorBinding.inflate(parent.layoutInflater, parent, false)
                .let(::ChatErrorViewHolder)
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: ChatMessageItemViewHolder, position: Int) {
        if (holder is ChatMessageViewHolder) {
            val item = data[position]
            holder.binding.apply {
                author.text = "Dano"
                text.text = item.text
            }
        }
    }

    override fun getItemCount(): Int {
        return when (adapterState) {
            AdapterState.LOADING, AdapterState.ERROR -> data.size + 1
            AdapterState.NORMAL -> data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < data.size) {
            ViewType.NORMAL
        } else {
            when (adapterState) {
                AdapterState.ERROR -> ViewType.ERROR
                AdapterState.LOADING -> ViewType.LOADING
                else -> throw RuntimeException("Position out of bounds")
            }
        }.ordinal
    }

    private fun getDiffCallback(items: List<ChatMessage>): DiffUtil.Callback {
        return SimpleDiffCallback(items, data)
    }

    fun submitState(pagingState: ChatPagingState.HasItemsState) {

        adapterState = when (pagingState) {
            is ChatPagingState.HasItemsState.Normal -> AdapterState.NORMAL
            is ChatPagingState.HasItemsState.LoadingNextPage -> AdapterState.LOADING
            is ChatPagingState.HasItemsState.Error -> AdapterState.ERROR
        }

        if (pagingState is ChatPagingState.HasItemsState.Normal) {
            pagingState.items.also {
                DiffUtil.calculateDiff(getDiffCallback(pagingState.items)).dispatchUpdatesTo(this)
                data.setAll(it)
            }
        }
    }

    private enum class ViewType {
        NORMAL, LOADING, ERROR
    }

    private enum class AdapterState {
        NORMAL, LOADING, ERROR
    }
}

sealed class ChatMessageItemViewHolder(binding: ViewDataBinding) : BaseViewHolder(binding)

data class ChatMessageViewHolder(override val binding: ItemChatmessageBinding) : ChatMessageItemViewHolder(binding)
data class ChatLoadingViewHolder(override val binding: ItemChatmessageLoadingBinding) : ChatMessageItemViewHolder(binding)
data class ChatErrorViewHolder(override val binding: ItemChatmessageErrorBinding) : ChatMessageItemViewHolder(binding)