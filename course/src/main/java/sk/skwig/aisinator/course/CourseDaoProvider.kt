package sk.skwig.aisinator.course

import sk.skwig.aisinator.course.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.course.db.roomdao.CourseworkDeadlineRoomDao

interface CourseDaoProvider {
    fun courseDao(): CourseRoomDao
    fun courseworkDeadlineDao(): CourseworkDeadlineRoomDao
}