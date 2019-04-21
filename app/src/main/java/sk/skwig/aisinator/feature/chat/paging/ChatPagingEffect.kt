package sk.skwig.aisinator.feature.chat.paging

import sk.skwig.aisinator.feature.chat.SearchQuery

sealed class ChatPagingEffect {
    object NoOp : ChatPagingEffect()
    data class Load(val query: SearchQuery) : ChatPagingEffect()
}
