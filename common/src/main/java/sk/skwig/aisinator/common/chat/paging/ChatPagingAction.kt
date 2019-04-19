package sk.skwig.aisinator.common.chat.paging

import sk.skwig.aisinator.common.chat.ChatMessage
import sk.skwig.aisinator.common.chat.SearchQuery

sealed class ChatPagingAction

sealed class UserAction : ChatPagingAction() {
    data class NewQuery(val query: SearchQuery) : UserAction()
    object NextPage : UserAction()
    object Retry : UserAction()
}

internal sealed class InternalAction : ChatPagingAction() {
    internal data class OnSuccess(val items: List<ChatMessage>) : InternalAction()
    internal data class OnError(val throwable: Throwable) : InternalAction()
}