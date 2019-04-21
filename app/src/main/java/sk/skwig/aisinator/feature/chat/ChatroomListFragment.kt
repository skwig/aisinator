package sk.skwig.aisinator.feature.chat

import android.os.Bundle
import android.view.View
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.util.listing.ListingFragment
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.chat.viewmodel.ChatroomListViewModel
import timber.log.Timber
import timber.log.Timber.e

class ChatroomListFragment : ListingFragment<ChatroomListViewModel, ChatroomAdapter, ChatroomViewHolder, Chatroom>() {

    override fun createAdapter() = ChatroomAdapter(viewModel::onChatroomSelected)

    override fun createViewModel() = Injector.injectChatroomListViewModel(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += viewModel.openChatroom.subscribeBy(
            onNext = {
                navController.navigate(ChatroomListFragmentDirections.actionChatroomListFragmentToChatroomFragment(it.id))
            },
            onError = Timber::e
        )
    }
}