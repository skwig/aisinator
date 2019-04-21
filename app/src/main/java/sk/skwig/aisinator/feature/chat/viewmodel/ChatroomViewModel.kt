package sk.skwig.aisinator.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.feature.chat.ChatRepository

class ChatroomViewModel(chatroomId: Long, chatRepository: ChatRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    init {
//        disposable += chatRepository.loadMessages()
//            .map { it.map { Chatroom(it) } }
//            .toViewState()
//            .subscribe(stateRelay)
    }
}