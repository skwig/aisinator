package sk.skwig.aisinator.dashboard

import sk.skwig.aisinator.dashboard.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao

interface DashboardDaoProvider {
    fun courseDao(): CourseRoomDao
    fun deadlineDao(): DeadlineRoomDao
}