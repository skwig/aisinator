package sk.skwig.aisinator.feature.timetable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.R
import sk.skwig.aisinator.common.util.dontDismissOnClick
import sk.skwig.aisinator.databinding.FragmentTimetableBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import timber.log.Timber

class TimetableFragment : BaseFragment<TimetableViewModel, FragmentTimetableBinding>() {

    private lateinit var timetableAdapter: TimetableAdapter
    private lateinit var filterState: TypeTimetableFilteringStrategy

    override fun createViewModel() = Injector.injectTimetableViewModel(this)

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentTimetableBinding {
        return FragmentTimetableBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            timetableAdapter = TimetableAdapter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            filter.setOnClickListener { filterButton ->
                PopupMenu(filterButton.context, filterButton).apply {
                    menuInflater.inflate(R.menu.menu_titlebar_filter, menu)
                    menu.setGroupCheckable(R.id.filter_group, true, false)
                    menu.findItem(R.id.show_lectures).isChecked = filterState.isShowingLectures
                    menu.findItem(R.id.show_seminars).isChecked = filterState.isShowingSeminars
                    menu.findItem(R.id.show_custom).isChecked = filterState.isShowingCustomItems

                    setOnMenuItemClickListener {
                        it.isChecked = !it.isChecked
                        it.dontDismissOnClick(context)

                        when (it.itemId) {
                            R.id.show_lectures -> viewModel.onFilterTimetable(filterState.copy(isShowingLectures = it.isChecked))
                            R.id.show_seminars -> viewModel.onFilterTimetable(filterState.copy(isShowingSeminars = it.isChecked))
                            R.id.show_custom -> viewModel.onFilterTimetable(filterState.copy(isShowingCustomItems = it.isChecked))
                        }

                        false
                    }
                }.show()
            }

            disposable += viewModel.uiState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        filterState = it.filterState
                        when (it) {
                            is TimetableViewModel.ViewState.Normal -> {
                                Log.d("matej", "onViewCreated() called ${it.timetableItems}")
                                title.text = it.timetableItems.joinToString(separator = "\n")
                            }
                        }
                    },
                    onError = Timber::e
                )
        }
    }
}
