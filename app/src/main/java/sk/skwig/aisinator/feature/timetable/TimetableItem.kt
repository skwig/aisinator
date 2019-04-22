package sk.skwig.aisinator.feature.timetable

import sk.skwig.aisinator.feature.lesson.Lesson
import java.time.temporal.ChronoUnit

data class TimetableItem(val lesson: Lesson) {

    val hourBlockCount: Int
        get() = let {
            val end = lesson.endTime.time
            val start = lesson.startTime.time

            end.until(start, ChronoUnit.HOURS).toInt()
        }
}