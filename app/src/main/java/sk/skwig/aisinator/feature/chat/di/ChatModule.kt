package sk.skwig.aisinator.feature.chat.di

import sk.skwig.aisinator.feature.chat.ChatRepository
import sk.skwig.aisinator.feature.chat.paging.ChatPager
import javax.inject.Singleton

class ChatModule {

    @Singleton
    val chatRepository: ChatRepository by lazy { ChatRepository() }

    val chatPager
        get() = ChatPager(chatRepository)
}
