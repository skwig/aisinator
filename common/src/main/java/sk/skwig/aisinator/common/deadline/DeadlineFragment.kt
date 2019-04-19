package sk.skwig.aisinator.common.deadline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.deadline.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.common.util.listing.DismissableListingFragment

class DeadlineFragment : DismissableListingFragment<DeadlinesViewModel, DeadlineAdapter, DeadlineViewHolder, Deadline>() {

    override fun createAdapter(): DeadlineAdapter {
        return DeadlineAdapter(viewModel::onDismiss)
    }

    override fun createViewModel(): DeadlinesViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(DeadlinesViewModel::class.java)
    }
}