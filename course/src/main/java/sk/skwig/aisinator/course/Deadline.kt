package sk.skwig.aisinator.course

data class Deadline(
    val id: Long,
    val course: Course,
    val name: String,
    val deadline: String,
    val isOpen: Boolean,
    val isDismissed: Boolean = false
)
