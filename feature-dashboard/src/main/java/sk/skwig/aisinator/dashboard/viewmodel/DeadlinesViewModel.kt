package sk.skwig.aisinator.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.dashboard.DashboardRepository
import timber.log.Timber
import javax.inject.Inject

class DeadlinesViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val stateRelay = BehaviorRelay.create<ViewState>()

    val state: Observable<ViewState>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += dashboardRepository.getActiveCourseworkDeadlines()
            .doOnError {
                Log.e("matej", ": ", it)
            }
            .doOnNext {
                Log.d("matej", "Deadline count: ${it.size} ")
            }
            .map { ViewState.Normal(it) as ViewState }
            .subscribe(stateRelay)
    }

    fun onDismiss(deadline: sk.skwig.aisinator.dashboard.Deadline){
        Log.d("matej", "onDismiss() called with: courseworkDeadline = [$deadline]")
        disposable += dashboardRepository.dismissCourseworkDeadline(deadline)
            .subscribeOn(Schedulers.io())
            .subscribe({}, Timber::e)
    }

    sealed class ViewState {
        data class Normal(val deadlines: List<sk.skwig.aisinator.dashboard.Deadline>) : ViewState()
    }
}