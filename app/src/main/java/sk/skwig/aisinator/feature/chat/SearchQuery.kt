package sk.skwig.aisinator.feature.chat

data class SearchQuery(val chatRoomId: Long, val page: Int = 0) {
    fun getNextPageQuery() = this.copy(page = this.page + 1)
}