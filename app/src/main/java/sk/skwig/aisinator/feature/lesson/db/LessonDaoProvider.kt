package sk.skwig.aisinator.feature.lesson.db

interface LessonDaoProvider {
    fun lessonDao(): LessonRoomDao
}