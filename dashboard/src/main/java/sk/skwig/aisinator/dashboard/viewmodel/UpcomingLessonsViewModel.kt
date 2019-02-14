package sk.skwig.aisinator.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.dashboard.DashboardRepository
import sk.skwig.aisinator.dashboard.UpcomingLesson
import javax.inject.Inject

class UpcomingLessonsViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val stateRelay = BehaviorRelay.create<ViewState>()

    val state: Observable<ViewState>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += dashboardRepository.getTodaysUpcomingLessons()
            .map<ViewState> { ViewState.Normal(it) }
            .subscribe(stateRelay)
    }

    sealed class ViewState {
        data class Normal(val upcomingLessons: List<UpcomingLesson>) : ViewState()
    }
}