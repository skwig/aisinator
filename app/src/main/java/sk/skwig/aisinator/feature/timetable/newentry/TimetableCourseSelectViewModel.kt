package sk.skwig.aisinator.feature.timetable.newentry

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.R
import sk.skwig.aisinator.common.util.listing.ListingViewModel
import sk.skwig.aisinator.feature.chat.Chatroom
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.course.CourseRepository

class TimetableCourseSelectViewModel(private val courseRepository: CourseRepository) : ListingViewModel<Course>() {

    private val selectCourseRelay = PublishRelay.create<Course>()

    val openTimetableItemCreation: Observable<Course>
        get() = selectCourseRelay

    init {
        disposable += courseRepository.getActiveCoursesWithoutLessons()
            .map { it + Course(-1, "CUSTOM", "CUSTOM", "18:00", true) }
            .toViewState(R.string.available_courses)
            .subscribe(stateRelay)
    }

    fun onCourseSelected(course: Course) {
        selectCourseRelay.accept(course)
    }
}