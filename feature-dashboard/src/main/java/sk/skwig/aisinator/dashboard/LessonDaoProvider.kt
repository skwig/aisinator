package sk.skwig.aisinator.dashboard

import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao

interface LessonDaoProvider {
    fun lessonDao(): LessonRoomDao
}