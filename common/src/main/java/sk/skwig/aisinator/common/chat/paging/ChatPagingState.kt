package sk.skwig.aisinator.common.chat.paging

import sk.skwig.aisinator.common.chat.ChatMessage
import sk.skwig.aisinator.common.chat.SearchQuery

sealed class ChatPagingState {

    abstract fun process(action: ChatPagingAction): Pair<ChatPagingState, ChatPagingEffect>

    protected fun noOp() = this to ChatPagingEffect.NoOp

    object InitialState : ChatPagingState() {
        override fun process(action: ChatPagingAction) =
            when (action) {
                is UserAction.NewQuery -> NoItemState.LoadingPage(
                    action.query
                ) to ChatPagingEffect.Load(action.query)
                else -> noOp()
            }
    }

    sealed class NoItemState(val query: SearchQuery) : ChatPagingState() {

        class LoadingPage(query: SearchQuery) : NoItemState(query) {
            override fun process(action: ChatPagingAction) =
                when (action) {
                    is MachineAction.OnSuccess -> HasItemsState.Normal(
                        query,
                        action.items
                    )
                    is MachineAction.OnError -> Error(
                        query,
                        action.throwable
                    )
                    else -> this
                } to ChatPagingEffect.NoOp
        }

        class Error(query: SearchQuery, val throwable: Throwable) : NoItemState(query) {
            override fun process(action: ChatPagingAction) =
                when (action) {
                    UserAction.Retry -> LoadingPage(
                        query
                    ) to ChatPagingEffect.Load(query)
                    else -> noOp()
                }
        }
    }

    sealed class HasItemsState(val query: SearchQuery, val items: List<ChatMessage>) : ChatPagingState() {

        class Normal(query: SearchQuery, items: List<ChatMessage>) : HasItemsState(query, items) {
            override fun process(action: ChatPagingAction) =
                when (action) {
                    UserAction.NextPage -> query.let {
                        val nextPageQuery = query.getNextPageQuery()
                        LoadingNextPage(
                            nextPageQuery,
                            items
                        ) to ChatPagingEffect.Load(nextPageQuery)
                    }
                    else -> noOp()
                }
        }

        class LoadingNextPage(query: SearchQuery, items: List<ChatMessage>) : HasItemsState(query, items) {
            override fun process(action: ChatPagingAction) =
                when (action) {
                    is MachineAction.OnSuccess -> Normal(
                        query,
                        items + action.items
                    )
                    is MachineAction.OnError -> Error(
                        query,
                        items,
                        action.throwable
                    )
                    else -> this
                } to ChatPagingEffect.NoOp
        }

        class Error(query: SearchQuery, items: List<ChatMessage>, val throwable: Throwable) : HasItemsState(query, items) {
            override fun process(action: ChatPagingAction) =
                when (action) {
                    UserAction.Retry -> NoItemState.LoadingPage(
                        query
                    ) to ChatPagingEffect.Load(query)
                    else -> noOp()
                }
        }
    }
}
