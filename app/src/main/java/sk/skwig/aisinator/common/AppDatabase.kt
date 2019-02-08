package sk.skwig.aisinator.common

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.skwig.aisinator.feature.course.CourseDao
import sk.skwig.aisinator.feature.course.CourseEntity
import sk.skwig.aisinator.feature.course.CourseworkDeadlineEntity

const val APP_DATABASE_NAME = "app-database"

@Database(
    version = 1,
    entities = [
        CourseEntity::class,
        CourseworkDeadlineEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}