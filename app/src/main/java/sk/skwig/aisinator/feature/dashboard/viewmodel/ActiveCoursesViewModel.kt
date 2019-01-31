package sk.skwig.aisinator.feature.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.course.CourseRepository
import javax.inject.Inject

class ActiveCoursesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    val state: Observable<ViewState>
        get() = courseRepository.getActiveCourses()
            .map { ViewState.Normal(it) as ViewState }

    private val disposable = CompositeDisposable()

    init {
        Log.d("matej", "DashboardViewModel INIT")
//        showLogin = Observable.merge(
//            showLoginRelay,
//            authMessageBus.events
//                .filter { it == AuthEvent.LoginNeeded }
//                .map { Unit }
//        )
//            .doOnSubscribe {
//                Log.d("matej", "showLogin.doOnSubscribe ()")
//            }

//        if (!settingsManager.hasLoginCredentials) {
//            disposable += Single.timer(50, TimeUnit.MILLISECONDS) // TODO: normalne riesenie
//                .map { Unit }
//                .subscribe(showLoginRelay)
//        }

//        disposable += authenticationManager.onAuthTimedOut
//            .subscribe { it ->
//                Log.d("matej", "$it")
//            }

        listOf("").forEach { }

//        Observable.timer(1, TimeUnit.SECONDS).subscribe {

//        disposable += courseRepository.getActiveCourses()
//            .map { it.first() }
//            .flatMap { courseRepository.getCoursework(it) }
//            .subscribe({
//                Log.d("matej", "$it")
//            }, {
//                Log.e("matej", "$it")
//            })
//        }
    }

    sealed class ViewState {
        data class Normal(val activeCourses: List<Course>) : ViewState()
    }
}