package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.chat.ChatRepository
import sk.skwig.aisinator.common.chat.paging.ChatPager
import javax.inject.Singleton

class ChatModule {

    @Singleton
    val chatRepository: ChatRepository by lazy { ChatRepository() }

    val chatPager
        get() = ChatPager(chatRepository)
}
