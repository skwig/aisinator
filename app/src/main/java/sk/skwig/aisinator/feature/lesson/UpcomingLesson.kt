package sk.skwig.aisinator.feature.lesson

import java.time.Instant

data class UpcomingLesson(
    val lesson: Lesson,
    val startTime: Instant,
    val endTime: Instant
)