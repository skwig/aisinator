package sk.skwig.aisinator.course

sealed class CourseFoo(
    open val course: Course,
    open val startTime: String,
    open val endTime: String,
    open val teacher: String,
    open val room: String
)

data class Seminar(
    override val course: Course,
    override val startTime: String,
    override val endTime: String,
    override val teacher: String,
    override val room: String
) : CourseFoo(course, startTime, endTime, teacher, room)

data class Lecture(
    override val course: Course,
    override val startTime: String,
    override val endTime: String,
    override val teacher: String,
    override val room: String
) : CourseFoo(course, startTime, endTime, teacher, room)