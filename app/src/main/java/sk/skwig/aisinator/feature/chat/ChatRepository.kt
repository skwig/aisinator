package sk.skwig.aisinator.feature.chat

import io.reactivex.Single

class ChatRepository {

    fun loadMessages(searchQuery: SearchQuery) : Single<List<ChatMessage>> = TODO()
}