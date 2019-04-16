package sk.skwig.aisinator.common.chat

data class SearchQuery(val chatRoomId: Long, val page: Int) {
    fun getNextPageQuery() = this.copy(page = this.page + 1)
}