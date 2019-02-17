package sk.skwig.aisinator.dashboard

import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao

interface DeadlineDaoProvider {
    fun deadlineDao(): DeadlineRoomDao
}