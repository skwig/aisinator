package sk.skwig.aisinator.feature.chat.paging

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.feature.chat.ChatRepository

class ChatPager(private val chatRepository: ChatRepository) {

    val state: Observable<ChatPagingState>

    private val actionRelay = PublishRelay.create<ChatPagingAction>()
    private val disposable = CompositeDisposable()

    init {
        val initialState = ChatPagingState.InitialState as ChatPagingState to ChatPagingEffect.NoOp as ChatPagingEffect

        val chatPagingState = actionRelay
            .scan(initialState) { previous, new -> previous.first.process(new) }
            .distinctUntilChanged()
            .share()

        disposable += chatPagingState
            .map { it.second }
            .ofType(ChatPagingEffect.Load::class.java)
            .concatMap {
                Log.d("matej", "ChatNextPage")
                chatRepository.loadMessages(it.query)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<InternalAction> { InternalAction.OnSuccess(it) }
                    .onErrorReturn { InternalAction.OnError(it) }
                    .subscribeOn(Schedulers.io())
            }
            .subscribe(actionRelay)

        state = chatPagingState
            .map { it.first }
            .distinctUntilChanged()
    }

    fun process(userAction: UserAction) {
        actionRelay.accept(userAction)
    }
}