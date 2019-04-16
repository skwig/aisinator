package sk.skwig.aisinator.common.chat.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.chat.ChatRepository
import sk.skwig.aisinator.common.chat.ChatViewModel
import sk.skwig.aisinator.common.chat.paging.ChatPager
import sk.skwig.aisinator.common.di.util.ViewModelKey
import javax.inject.Singleton

@Module(includes = [ChatViewModelModule::class, ChatFragmentModule::class])
class ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository() = ChatRepository()

    @Provides
    fun provideChatPager(chatRepository: ChatRepository) = ChatPager(chatRepository)
}

@Module
abstract class ChatViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel
}

@Module
abstract class ChatFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeChatFragment(): ChatFragment

}