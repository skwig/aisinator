package sk.skwig.aisinator

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.skwig.aisinator.course.CourseDaoProvider
import sk.skwig.aisinator.course.db.entity.CourseEntity
import sk.skwig.aisinator.course.db.entity.CourseworkDeadlineEntity
import sk.skwig.aisinator.course.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.course.db.roomdao.CourseworkDeadlineRoomDao

const val APP_DATABASE_NAME = "app-database"

@Database(
    version = 1,

    entities = [
        CourseEntity::class,
        CourseworkDeadlineEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase(), CourseDaoProvider {
    abstract override fun courseDao(): CourseRoomDao
    abstract override fun courseworkDeadlineDao(): CourseworkDeadlineRoomDao
}