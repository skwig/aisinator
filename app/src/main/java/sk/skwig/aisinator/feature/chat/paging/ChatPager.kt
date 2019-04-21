package sk.skwig.aisinator.feature.chat.paging

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.feature.chat.ChatRepository

class ChatPager(private val chatRepository: ChatRepository) {

    val state: Observable<ChatPagingState>

    private val actionRelay = BehaviorRelay.create<ChatPagingAction>()
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
                chatRepository.loadMessages(it.query)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<InternalAction> {
                        InternalAction.OnSuccess(
                            it
                        )
                    }
                    .onErrorReturn { InternalAction.OnError(it) }
                    .subscribeOn(Schedulers.io())
            }
            .subscribe(actionRelay)

        state = chatPagingState
            .map { it.first }
            .distinctUntilChanged()
    }
}