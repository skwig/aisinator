package sk.skwig.aisinator.feature.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import sk.skwig.aisinator.databinding.FragmentChatroomBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.chat.viewmodel.ChatroomViewModel

class ChatroomFragment : BaseFragment<ChatroomViewModel, FragmentChatroomBinding>() {
    override fun createViewModel(): ChatroomViewModel {
        return Injector.injectChatroomViewModel(this, 0L)
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentChatroomBinding {
        return FragmentChatroomBinding.inflate(layoutInflater, container, false)
    }
}