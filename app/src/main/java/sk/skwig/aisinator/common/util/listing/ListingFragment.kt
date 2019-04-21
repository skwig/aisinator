package sk.skwig.aisinator.common.util.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_listing.*
import sk.skwig.aisinator.common.util.showChild
import sk.skwig.aisinator.databinding.FragmentListingBinding
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.BaseViewHolder
import timber.log.Timber

abstract class ListingFragment<VM : ListingViewModel<T>, A : ListingAdapter<T, VH>, VH : BaseViewHolder, T> :
    BaseFragment<VM, FragmentListingBinding>() {

    protected val adapter: A by lazy(::createAdapter)

    abstract fun createAdapter(): A

    open fun createLayoutManager() = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += viewModel.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it) {
                        is ListingViewModel.ViewState.Displaying<T> -> {
                            binding.viewAnimator.showChild(binding.recyclerView)
                            adapter.submitList(it.items)
                        }
                        is ListingViewModel.ViewState.Loading<T> -> {
                            binding.viewAnimator.showChild(binding.progressBar)
                        }
                    }
                },
                onError = Timber::e
            )

        binding.apply {
            title.text = "Lol"
            recyclerView.adapter = adapter
            recyclerView.layoutManager = createLayoutManager()
        }
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentListingBinding {
        return FragmentListingBinding.inflate(layoutInflater, container, false)
    }
}

abstract class DismissableListingFragment<VM : DismissableListingViewModel<T>, A : DismissableListingAdapter<T, VH>, VH : BaseViewHolder, T> :
    ListingFragment<VM, A, VH, T>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.onSwiped(viewHolder, direction)
                }
            }).attachToRecyclerView(recyclerView)
        }
    }
}