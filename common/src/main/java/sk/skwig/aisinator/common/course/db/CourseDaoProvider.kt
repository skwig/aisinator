package sk.skwig.aisinator.common.course.db

interface CourseDaoProvider {
    fun courseDao(): CourseRoomDao
}