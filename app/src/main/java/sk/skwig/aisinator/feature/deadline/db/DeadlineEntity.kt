package sk.skwig.aisinator.feature.deadline.db

import androidx.room.*
import sk.skwig.aisinator.feature.course.db.CourseEntity
import java.time.Instant

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

// join je potrebne robit rucne
data class DeadlineWithCourse(

    @Embedded
    val deadline: DeadlineEntity,

    @Embedded
    val course: CourseEntity
)