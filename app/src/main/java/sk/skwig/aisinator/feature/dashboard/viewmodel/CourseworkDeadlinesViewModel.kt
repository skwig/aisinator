package sk.skwig.aisinator.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.course.CourseRepository
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import javax.inject.Inject

class CourseworkDeadlinesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val stateRelay = BehaviorRelay.create<ViewState>()

    val state: Observable<ViewState>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
//        disposable += courseRepository.getActiveCourseworkDeadlines()
//            .map { ViewState.Normal(it) as ViewState }
//            .subscribe(stateRelay)
    }

    sealed class ViewState {
        data class Normal(val deadlines: List<CourseworkDeadline>) : ViewState()
    }
}