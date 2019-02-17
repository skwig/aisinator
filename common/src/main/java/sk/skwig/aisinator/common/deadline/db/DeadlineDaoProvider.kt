package sk.skwig.aisinator.common.deadline.db

interface DeadlineDaoProvider {
    fun deadlineDao(): DeadlineRoomDao
}