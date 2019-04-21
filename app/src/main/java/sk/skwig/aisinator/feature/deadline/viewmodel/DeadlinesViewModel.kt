package sk.skwig.aisinator.feature.deadline.viewmodel

import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.deadline.Deadline
import sk.skwig.aisinator.feature.deadline.DeadlineRepository
import sk.skwig.aisinator.common.util.listing.DismissableListingViewModel

class DeadlinesViewModel(private val deadlineRepository: DeadlineRepository) : DismissableListingViewModel<Deadline>() {

    init {
        disposable += deadlineRepository.getActiveDeadlines()
            .toViewState()
            .subscribe(stateRelay)
    }

    override fun dismissCompletable(item: Deadline) = deadlineRepository.dismissDeadline(item)
}