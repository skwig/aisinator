package sk.skwig.aisinator.common.data

import java.time.Instant

data class UpcomingLesson(
    val lesson: Lesson,
    val startTime: Instant,
    val endTime: Instant
)