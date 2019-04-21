package sk.skwig.aisinator.feature.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.feature.chat.paging.ChatPagingAction
import sk.skwig.aisinator.feature.chat.paging.ChatPagingEffect
import sk.skwig.aisinator.feature.chat.paging.ChatPagingState
import sk.skwig.aisinator.feature.chat.paging.InternalAction

class ChatViewModel(val chatRepository: ChatRepository) : ViewModel() {

    private val uiStateRelay = BehaviorRelay.create<ChatPagingState>()

    private val actionRelay = BehaviorRelay.create<ChatPagingAction>()

    private val disposable = CompositeDisposable()

    val uiState: Observable<ChatPagingState>
        get() = uiStateRelay

    init {

        val initialState = ChatPagingState.InitialState as ChatPagingState to ChatPagingEffect.NoOp as ChatPagingEffect

        val chatPagingState = actionRelay
            .scan(initialState) { acc, item -> acc.first.process(item) }
            .distinctUntilChanged()
            .share()

        disposable += chatPagingState
            .map { it.first }
            .distinctUntilChanged()
            .subscribe(uiStateRelay)

        disposable += chatPagingState
            .map { it.second }
            .filter { it != ChatPagingEffect.NoOp }
            .concatMap {
                val effect = it as ChatPagingEffect.Load
                chatRepository.loadMessages(effect.query)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .onErrorResumeNext { throwable: Throwable ->
                        actionRelay.accept(InternalAction.OnError(throwable))
                        Observable.empty<List<ChatMessage>>()
                    }
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({
                actionRelay.accept(InternalAction.OnSuccess(it))
            }, { Log.e("default", "", it) })
    }

}