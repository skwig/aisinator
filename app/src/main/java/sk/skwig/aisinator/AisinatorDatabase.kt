package sk.skwig.aisinator

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.skwig.aisinator.course.CourseDaoProvider
import sk.skwig.aisinator.course.db.entity.CourseEntity
import sk.skwig.aisinator.course.db.entity.DeadlineEntity

const val APP_DATABASE_NAME = "aisinator-database"

@Database(
    version = 1,
    entities = [
        CourseEntity::class,
        DeadlineEntity::class
    ]
)
abstract class AisinatorDatabase : RoomDatabase(), CourseDaoProvider