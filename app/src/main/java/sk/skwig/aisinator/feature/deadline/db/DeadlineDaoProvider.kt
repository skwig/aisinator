package sk.skwig.aisinator.feature.deadline.db

interface DeadlineDaoProvider {
    fun deadlineDao(): DeadlineRoomDao
}