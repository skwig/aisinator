package sk.skwig.aisinator

import androidx.room.*
import sk.skwig.aisinator.common.util.DayOfWeekConverter
import sk.skwig.aisinator.common.util.InstantConverter
import sk.skwig.aisinator.common.util.LocalTimeConverter
import sk.skwig.aisinator.feature.course.db.CourseDaoProvider
import sk.skwig.aisinator.feature.course.db.CourseEntity
import sk.skwig.aisinator.feature.deadline.db.DeadlineDaoProvider
import sk.skwig.aisinator.feature.deadline.db.DeadlineEntity
import sk.skwig.aisinator.feature.lesson.LessonEntity
import sk.skwig.aisinator.feature.lesson.db.LessonDaoProvider

const val APP_DATABASE_NAME = "aisinator-database"

@Database(
    version = 1,
    entities = [
        CourseEntity::class,
        DeadlineEntity::class,
        LessonEntity::class,
        TestEntity::class
    ]
)
@TypeConverters(
    value = [
        InstantConverter::class,
        DayOfWeekConverter::class,
        LocalTimeConverter::class,
        FooStateConverter::class
    ]
)
abstract class AisinatorDatabase : RoomDatabase(), CourseDaoProvider,
    DeadlineDaoProvider, LessonDaoProvider

enum class FooState {
    FOO, BAR, BUZZ
}

@Entity
class TestEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "state")
    val fooState: FooState
)

object FooStateConverter {
    @JvmStatic
    @TypeConverter
    fun fromColumn(column: String?): FooState? {
        return column?.let { FooState.valueOf(it) }
    }

    @JvmStatic
    @TypeConverter
    fun toColumn(localTime: FooState?): String? {
        return localTime?.toString()
    }
}