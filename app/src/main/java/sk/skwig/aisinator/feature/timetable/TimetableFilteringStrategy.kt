package sk.skwig.aisinator.feature.timetable

interface TimetableFilteringStrategy {
    fun allows(timetableItem: TimetableItem): Boolean
}

class DefaultTimetableFilteringStrategy : TimetableFilteringStrategy {
    override fun allows(timetableItem: TimetableItem): Boolean {
        return true
    }
}

class NameTimetableFilteringStrategy(
    private val requiredSubstring: String,
    private val ignoreCase: Boolean = true
) : TimetableFilteringStrategy {
    override fun allows(timetableItem: TimetableItem): Boolean {
        return timetableItem.lesson.course.name.contains(requiredSubstring, ignoreCase)
    }
}