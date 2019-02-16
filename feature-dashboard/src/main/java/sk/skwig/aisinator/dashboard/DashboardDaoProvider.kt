package sk.skwig.aisinator.dashboard

import sk.skwig.aisinator.dashboard.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao

interface DashboardDaoProvider {
    fun courseDao(): CourseRoomDao
    fun deadlineDao(): DeadlineRoomDao
    fun lessonDao(): LessonRoomDao
}