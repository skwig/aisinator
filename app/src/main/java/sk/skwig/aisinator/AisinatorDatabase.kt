package sk.skwig.aisinator

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.skwig.aisinator.dashboard.CourseDaoProvider
import sk.skwig.aisinator.dashboard.DeadlineDaoProvider
import sk.skwig.aisinator.dashboard.LessonDaoProvider
import sk.skwig.aisinator.dashboard.db.entity.CourseEntity
import sk.skwig.aisinator.dashboard.db.entity.DeadlineEntity
import sk.skwig.aisinator.dashboard.db.entity.LessonEntity

const val APP_DATABASE_NAME = "aisinator-database"

@Database(
    version = 1,
    entities = [
        CourseEntity::class,
        DeadlineEntity::class,
        LessonEntity::class
    ]
)
@TypeConverters(
    value = [
        InstantConverter::class,
        DayOfWeekConverter::class,
        LocalTimeConverter::class
    ]
)
abstract class AisinatorDatabase : RoomDatabase(), CourseDaoProvider, DeadlineDaoProvider, LessonDaoProvider