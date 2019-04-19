package sk.skwig.aisinator.common.lesson.viewmodel

import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.common.data.UpcomingLesson
import sk.skwig.aisinator.common.lesson.LessonRepository
import sk.skwig.aisinator.common.util.listing.ListingViewModel

class UpcomingLessonsViewModel(lessonRepository: LessonRepository) : ListingViewModel<UpcomingLesson>() {

    init {
        disposable += lessonRepository.getTodaysUpcomingLessons()
            .toViewState()
            .subscribe(stateRelay)
    }
}