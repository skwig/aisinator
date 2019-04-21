package sk.skwig.aisinator.common.util.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.util.showChild
import sk.skwig.aisinator.databinding.FragmentListingBinding
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.BaseViewHolder
import timber.log.Timber

class A<T> : ListingViewModel<T>()

abstract class ListingFragment<VM : ListingViewModel<T>, A : ListingAdapter<T, VH>, VH : BaseViewHolder, T> :
    BaseFragment<VM, FragmentListingBinding>(){

    protected val adapter: A by lazy(::createAdapter)

    abstract fun createAdapter(): A

    open fun createLayoutManager() = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            recyclerView.adapter = adapter
            recyclerView.layoutManager = createLayoutManager()

            disposable += viewModel.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        title.setText(it.title)
                        when (it) {
                            is ListingViewModel.ViewState.Displaying<T> -> {
                                viewAnimator.showChild(recyclerView)
                                adapter.submitList(it.items)
                            }
                            is ListingViewModel.ViewState.Loading<T> -> {
                                viewAnimator.showChild(progressBar)
                            }
                        }
                    },
                    onError = Timber::e
                )
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