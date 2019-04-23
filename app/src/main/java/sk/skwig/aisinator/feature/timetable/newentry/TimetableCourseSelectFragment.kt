package sk.skwig.aisinator.feature.timetable.newentry

import sk.skwig.aisinator.common.util.listing.ListingFragment
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.course.Course

class TimetableCourseSelectFragment :
    ListingFragment<TimetableCourseSelectViewModel, TimetableCourseSelectAdapter, TimetableCourseSelectViewHolder, Course>() {
    override fun createAdapter(): TimetableCourseSelectAdapter {
        return TimetableCourseSelectAdapter()
    }

    override fun createViewModel(): TimetableCourseSelectViewModel {
        return Injector.injectTimetableCourseSelectViewModel(this)
    }
}