package sk.skwig.aisinator

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.skwig.aisinator.common.course.db.CourseDaoProvider
import sk.skwig.aisinator.common.data.db.CourseEntity
import sk.skwig.aisinator.common.data.db.DeadlineEntity
import sk.skwig.aisinator.common.data.db.LessonEntity
import sk.skwig.aisinator.common.deadline.db.DeadlineDaoProvider
import sk.skwig.aisinator.common.lesson.db.LessonDaoProvider

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
abstract class AisinatorDatabase : RoomDatabase(), CourseDaoProvider,
    DeadlineDaoProvider, LessonDaoProvider