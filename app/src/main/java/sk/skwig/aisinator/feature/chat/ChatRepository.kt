package sk.skwig.aisinator.feature.chat

import io.reactivex.Single
import java.util.concurrent.TimeUnit

class ChatRepository {

    fun loadMessages(searchQuery: SearchQuery) : Single<List<ChatMessage>> = Single.just((0..10).map { ChatMessage("Message $it Query $searchQuery") }).delay(1, TimeUnit.SECONDS)
}