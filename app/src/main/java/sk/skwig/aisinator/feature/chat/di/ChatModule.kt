package sk.skwig.aisinator.feature.chat.di

import sk.skwig.aisinator.di.NetworkModule
import sk.skwig.aisinator.feature.chat.ChatRepository
import sk.skwig.aisinator.feature.chat.api.ChatRetrofitApi
import sk.skwig.aisinator.feature.chat.paging.ChatPager
import javax.inject.Singleton

class ChatModule {

    lateinit var networkModule: NetworkModule

    @Singleton
    val chatRepository: ChatRepository by lazy { ChatRepository().apply{chatRetrofitApi} }

    @Singleton
    val chatRetrofitApi : ChatRetrofitApi by lazy {
        networkModule.dashboardRetrofit.create(ChatRetrofitApi::class.java)
    }

    val chatPager
        get() = ChatPager(chatRepository)
}
