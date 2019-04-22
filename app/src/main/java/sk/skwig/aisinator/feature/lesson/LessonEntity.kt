package sk.skwig.aisinator.feature.lesson

import androidx.room.*
import sk.skwig.aisinator.feature.course.db.CourseEntity
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime

@Entity(
    tableName = "lessons",
    primaryKeys = ["fk_course_id", "lesson_is_lecture"],
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
data class LessonEntity(
    @ColumnInfo(name = "fk_course_id")
    val courseId: Long,

    @ColumnInfo(name = "lesson_room")
    val room: String,

    @ColumnInfo(name = "lesson_teacher")
    val teacher: String,

    @ColumnInfo(name = "lesson_is_lecture")
    val isLecture: Boolean,

    @Embedded(prefix = "lesson_start_")
    val startTime: LessonTimeEntity,

    @Embedded(prefix = "lesson_end_")
    val endTime: LessonTimeEntity
)


data class LessonTimeEntity(
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: DayOfWeek,

    @ColumnInfo(name = "time")
    val time: LocalTime
)

data class UpcomingLessonEntity(
    @Embedded
    val lessonWithCourse: LessonWithCourse,

    @ColumnInfo(name = "upcoming_start")
    val startTime: Instant,

    @ColumnInfo(name = "upcoming_end")
    val endTime: Instant
)

// join je potrebne robit rucne
data class LessonWithCourse(

    @Embedded
    val lesson: LessonEntity,

    @Embedded
    val course: CourseEntity
)