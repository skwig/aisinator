package sk.skwig.aisinator

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.skwig.aisinator.dashboard.DashboardDaoProvider
import sk.skwig.aisinator.dashboard.db.entity.CourseEntity
import sk.skwig.aisinator.dashboard.db.entity.DeadlineEntity

const val APP_DATABASE_NAME = "aisinator-database"

@Database(
    version = 1,
    entities = [
        CourseEntity::class,
        DeadlineEntity::class
    ]
)
abstract class AisinatorDatabase : RoomDatabase(), DashboardDaoProvider