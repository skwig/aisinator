package sk.skwig.aisinator.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.feature.course.CourseRepository
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import javax.inject.Inject

class CourseworkDeadlinesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    val state: Observable<ViewState>
        get() = courseRepository.getCourseworkDeadlines()
            .map { ViewState.Normal(it) as ViewState }

    private val disposable = CompositeDisposable()

    sealed class ViewState {
        data class Normal(val deadlines: List<CourseworkDeadline>) : ViewState()
    }
}