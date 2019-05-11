package sk.skwig.aisinator.feature.lesson.viewmodel

import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.R
import sk.skwig.aisinator.feature.lesson.UpcomingLesson
import sk.skwig.aisinator.feature.lesson.LessonRepository
import sk.skwig.aisinator.common.util.listing.ListingViewModel

class UpcomingLessonsViewModel(lessonRepository: LessonRepository) : ListingViewModel<UpcomingLesson>() {

    init {
        disposable += Observable.just(emptyList<UpcomingLesson>())
            .toViewState(R.string.upcoming_lessons)
            .subscribe(stateRelay)
    }
}