package sk.skwig.aisinator.timetable

import androidx.lifecycle.ViewModel
import sk.skwig.aisinator.common.lesson.LessonRepository
import javax.inject.Inject

// TODO: checkboxy na prednasky a cvicenia
// TODO: prazdne columny & rowy collapsnut nech nezaberaju tolko miesta

class TimetableViewModel @Inject constructor(
    private val lessonRepository: LessonRepository
) : ViewModel() {

}