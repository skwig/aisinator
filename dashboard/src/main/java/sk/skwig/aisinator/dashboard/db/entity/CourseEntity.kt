package sk.skwig.aisinator.dashboard.db.entity

import androidx.room.*
import java.time.Instant

// naming convention:
// 1. prefixovat foreign keye "fk"
// 2. prefixovat columny nazvom tabulky
//      - room nezvlada duplikaty column namov v embednutom query resulte a prefix anotacia mu nestaci

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey
    @ColumnInfo(name = "course_id")
    val id: Long,

    @ColumnInfo(name = "course_tag")
    val tag: String,

    @ColumnInfo(name = "course_name")
    val name: String,

    @ColumnInfo(name = "course_time")
    val time: String,

    @ColumnInfo(name = "course_is_active")
    val isActive: Boolean
)

@Entity(
    tableName = "deadlines",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["course_id"],
            childColumns = ["fk_course_id"]
        )
    ],
    indices = [
        Index(
            value = ["fk_course_id"]
        )
    ]
)
data class DeadlineEntity(
    @PrimaryKey
    @ColumnInfo(name = "deadline_id")
    val id: Long,

    @ColumnInfo(name = "fk_course_id")
    val courseId: Long,

    @ColumnInfo(name = "deadline_name")
    val name: String,

    @ColumnInfo(name = "deadline_deadline")
    val deadline: Instant,

    @ColumnInfo(name = "deadline_is_open")
    val isOpen: Boolean,

    @ColumnInfo(name = "deadline_is_dismissed")
    val isDismissed: Boolean
)