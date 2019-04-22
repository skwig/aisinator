package sk.skwig.aisinator.feature.timetable

import sk.skwig.aisinator.feature.lesson.Lesson

interface TimetableFilteringStrategy {
    fun allows(timetableItem: TimetableItem): Boolean
}

class DefaultTimetableFilteringStrategy : TimetableFilteringStrategy {
    override fun allows(timetableItem: TimetableItem): Boolean {
        return true
    }
}

class CourseNameTimetableFilteringStrategy(
    private val requiredSubstring: String,
    private val ignoreCase: Boolean = true
) : TimetableFilteringStrategy {
    override fun allows(timetableItem: TimetableItem): Boolean {
        return timetableItem.lesson.course.name.contains(requiredSubstring, ignoreCase)
    }
}

