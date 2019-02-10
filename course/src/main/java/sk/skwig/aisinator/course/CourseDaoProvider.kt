package sk.skwig.aisinator.course

import sk.skwig.aisinator.course.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.course.db.roomdao.DeadlineRoomDao

interface CourseDaoProvider {
    fun courseDao(): CourseRoomDao
    fun courseworkDeadlineDao(): DeadlineRoomDao
}