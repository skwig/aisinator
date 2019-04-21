package sk.skwig.aisinator.feature.lesson

import sk.skwig.aisinator.common.util.listing.ListingFragment
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.lesson.viewmodel.UpcomingLessonsViewModel

class UpcomingLessonsFragment :
    ListingFragment<UpcomingLessonsViewModel, UpcomingLessonsAdapter, UpcomingLessonViewHolder, UpcomingLesson>() {

    override fun createViewModel(): UpcomingLessonsViewModel {
        return Injector.injectUpcomingLessonsViewModel(this)
    }

    override fun createAdapter(): UpcomingLessonsAdapter {
        return UpcomingLessonsAdapter()
    }
}