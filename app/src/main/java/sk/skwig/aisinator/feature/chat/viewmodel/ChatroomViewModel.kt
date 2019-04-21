package sk.skwig.aisinator.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.chat.SearchQuery
import sk.skwig.aisinator.feature.chat.paging.ChatPager
import sk.skwig.aisinator.feature.chat.paging.ChatPagingState
import sk.skwig.aisinator.feature.chat.paging.UserAction

class ChatroomViewModel(chatroomId: Long, private val chatPager: ChatPager) : ViewModel() {

    private val uiStateRelay = BehaviorRelay.create<ChatPagingState>()

    val uiState: Observable<ChatPagingState>
        get() = uiStateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += chatPager.state
            .subscribe(uiStateRelay)

        chatPager.process(UserAction.NewQuery(SearchQuery(chatroomId)))
    }

    fun onNextPage() {
        chatPager.process(UserAction.NextPage)
    }

    fun onRetry() {
        chatPager.process(UserAction.Retry)
    }
}