package sk.skwig.aisinator.feature.lesson

import sk.skwig.aisinator.feature.course.Course
import java.time.DayOfWeek
import java.time.LocalTime

sealed class Lesson(
    open val course: Course,
    open val startTime: LessonTime,
    open val endTime: LessonTime,
    open val teacher: String,
    open val room: String,
    open val isCustom: Boolean = false
)


{
    data class Seminar(
        override val course: Course,
        override val startTime: LessonTime,
        override val endTime: LessonTime,
        override val teacher: String,
        override val room: String
    ) : Lesson(course, startTime, endTime, teacher, room)

    data class Lecture(
        override val course: Course,
        override val startTime: LessonTime,
        override val endTime: LessonTime,
        override val teacher: String,
        override val room: String
    ) : Lesson(course, startTime, endTime, teacher, room)
}

data class LessonTime(
    val dayOfWeek: DayOfWeek,
    val time: LocalTime
)