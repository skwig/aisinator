package sk.skwig.aisinator.common.lesson.db

interface LessonDaoProvider {
    fun lessonDao(): LessonRoomDao
}