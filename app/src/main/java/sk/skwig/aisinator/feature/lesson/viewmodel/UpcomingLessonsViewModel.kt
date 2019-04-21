package sk.skwig.aisinator.feature.lesson.viewmodel

import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.lesson.UpcomingLesson
import sk.skwig.aisinator.feature.lesson.LessonRepository
import sk.skwig.aisinator.common.util.listing.ListingViewModel

class UpcomingLessonsViewModel(lessonRepository: LessonRepository) : ListingViewModel<UpcomingLesson>() {

    init {
        disposable += lessonRepository.getTodaysUpcomingLessons()
            .toViewState()
            .subscribe(stateRelay)
    }
}