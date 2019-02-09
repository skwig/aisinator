package sk.skwig.aisinator.feature.course.db.entity

import androidx.room.*

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "tag")
    val tag: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean
)

@Entity(
    tableName = "coursework_deadlines",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["course_id"]
        )
    ],
    indices = [
        Index(
            value = ["course_id"]
        )
    ]
)
data class CourseworkDeadlineEntity(
    @PrimaryKey
    @ColumnInfo(name = "d_id")
    val id: Long,

    @ColumnInfo(name = "course_id")
    val courseId: Long,

    @ColumnInfo(name = "d_name")
    val name: String,

    @ColumnInfo(name = "deadline")
    val deadline: String,

    @ColumnInfo(name = "is_open")
    val isOpen: Boolean,

    @ColumnInfo(name = "is_dismissed")
    val isDismissed: Boolean
)