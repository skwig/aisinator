package sk.skwig.aisinator.common.data

import java.time.Instant

data class Deadline(
    val id: Long,
    val course: Course,
    val name: String,
    val deadline : Instant,
    val isOpen: Boolean,
    val isDismissed: Boolean = false
)
