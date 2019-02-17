package sk.skwig.aisinator.dashboard

import sk.skwig.aisinator.dashboard.db.roomdao.CourseRoomDao

interface CourseDaoProvider {
    fun courseDao(): CourseRoomDao
}