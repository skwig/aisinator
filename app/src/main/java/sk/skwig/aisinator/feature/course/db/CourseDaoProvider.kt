package sk.skwig.aisinator.feature.course.db

interface CourseDaoProvider {
    fun courseDao(): CourseRoomDao
}