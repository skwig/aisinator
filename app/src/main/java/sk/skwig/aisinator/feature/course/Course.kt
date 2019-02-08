package sk.skwig.aisinator.feature.course

data class Course(
    val id: Long,
    val tag: String,
    val name: String,
    val time: String,
    val isActive:Boolean
    /*attendance*/
)

data class Coursework(
    val entries: List<CourseworkEntry>
)

data class CourseworkEntry(
    val name: String,
    val values: Map<String, String>
)

data class CourseworkDeadline(
    val id: Long,
    val course: Course,
    val name: String,
    val deadline: String,
    val isOpen: Boolean,
    val isDismissed: Boolean = false
)
