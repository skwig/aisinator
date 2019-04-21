package sk.skwig.aisinator.feature.deadline

import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.deadline.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.common.util.listing.DismissableListingFragment

class DeadlineFragment : DismissableListingFragment<DeadlinesViewModel, DeadlineAdapter, DeadlineViewHolder, Deadline>() {

    override fun createAdapter(): DeadlineAdapter {
        return DeadlineAdapter(viewModel::onDismiss)
    }

    override fun createViewModel(): DeadlinesViewModel = Injector.injectDeadlineViewModel(this)
}