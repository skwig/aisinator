package sk.skwig.aisinator.course.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.course.CourseRepository
import sk.skwig.aisinator.course.Deadline
import timber.log.Timber
import javax.inject.Inject

class CourseworkDeadlinesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val stateRelay = BehaviorRelay.create<ViewState>()

    val state: Observable<ViewState>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += courseRepository.getActiveCourseworkDeadlines()
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
        disposable += courseRepository.dismissCourseworkDeadline(deadline)
            .subscribeOn(Schedulers.io())
            .subscribe({}, Timber::e)
    }

    sealed class ViewState {
        data class Normal(val deadlines: List<Deadline>) : ViewState()
    }
}