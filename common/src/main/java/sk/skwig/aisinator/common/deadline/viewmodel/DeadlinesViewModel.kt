package sk.skwig.aisinator.common.deadline.viewmodel

import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.deadline.DeadlineRepository
import sk.skwig.aisinator.common.util.listing.DismissableListingViewModel
import javax.inject.Inject

class DeadlinesViewModel @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) : DismissableListingViewModel<Deadline>() {

    init {
        disposable += deadlineRepository.getActiveDeadlines()
            .toViewState()
            .subscribe(stateRelay)
    }

    override fun dismissCompletable(item: Deadline) = deadlineRepository.dismissDeadline(item)
}