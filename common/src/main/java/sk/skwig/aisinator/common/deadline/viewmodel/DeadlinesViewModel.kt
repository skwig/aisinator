package sk.skwig.aisinator.common.deadline.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.deadline.DeadlineRepository
import timber.log.Timber
import javax.inject.Inject

class DeadlinesViewModel @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) : ViewModel() {

    private val stateRelay = BehaviorRelay.create<ViewState>()

    val state: Observable<ViewState>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += deadlineRepository.getActiveDeadlines()
            .doOnError {
                Log.e("matej", ": ", it)
            }
            .doOnNext {
                Log.d("matej", "Deadline count: ${it.size} ")
            }
            .map { ViewState.Normal(it) as ViewState }
            .subscribe(stateRelay)
    }

    fun onDismiss(deadline: Deadline){
        Log.d("matej", "onDismiss() called with: courseworkDeadline = [$deadline]")
        disposable += deadlineRepository.dismissDeadline(deadline)
            .subscribeOn(Schedulers.io())
            .subscribe({}, Timber::e)
    }

    sealed class ViewState {
        data class Normal(val deadlines: List<Deadline>) : ViewState()
    }
}