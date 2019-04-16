package sk.skwig.aisinator.common.chat

import io.reactivex.Single

class ChatRepository {

    fun loadMessages(searchQuery: SearchQuery) : Single<List<ChatMessage>> = TODO()
}