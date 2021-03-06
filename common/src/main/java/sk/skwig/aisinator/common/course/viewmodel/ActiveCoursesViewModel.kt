package sk.skwig.aisinator.common.course.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.common.course.CourseRepository
import sk.skwig.aisinator.common.data.Course
import timber.log.Timber
import javax.inject.Inject

class ActiveCoursesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    val state: Observable<ViewState>


    private val disposable = CompositeDisposable()

    init {

        // TODO: fungujuci startWith

        state = courseRepository.getActiveCourses()
            .map<ViewState> {
                ViewState.Normal(
                    it
                )
            }
            .startWith(ViewState.Loading)
            .doOnError(Timber::e)
            .onErrorReturnItem(ViewState.Error)
            .timestamp()
            .doOnNext {
                Log.d("matej", "${it.time()}: ${it.value()}")
            }
            .map { it.value() }
    }

    sealed class ViewState {
        data class Normal(val activeCourses: List<Course>) : ViewState()
        object Loading : ViewState()
        object Error : ViewState()
    }
}