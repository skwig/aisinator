package sk.skwig.aisinator.common.chat.paging

import sk.skwig.aisinator.common.chat.SearchQuery

sealed class ChatPagingEffect {
    object NoOp : ChatPagingEffect()
    data class Load(val query: SearchQuery) : ChatPagingEffect()
}
