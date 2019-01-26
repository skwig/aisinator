package sk.skwig.aisinator.feature.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.auth.AuthEvent
import sk.skwig.aisinator.feature.auth.AuthEventBus
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.course.CourseRepository
import sk.skwig.aisinator.feature.settings.SettingsManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val authEventBus: AuthEventBus,
    private val courseRepository: CourseRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val showLoginRelay = PublishRelay.create<Unit>()

    val showLogin: Observable<Unit>

    init {

        showLogin = Observable.merge(
            showLoginRelay,
            authEventBus.events
                .filter { it == AuthEvent.LoginNeeded }
                .map { Unit }
        )

        if (!settingsManager.hasLoginCredentials) {
            disposable += Single.timer(50, TimeUnit.MILLISECONDS) // TODO: normalne riesenie
                .map { Unit }
                .subscribe(showLoginRelay)
        }

//        disposable += authenticationManager.onAuthTimedOut
//            .subscribe { it ->
//                Log.d("matej", "$it")
//            }

//        disposable += courseRepository.getActiveCourses()
//            .map { it.first() }
//            .flatMap { courseRepository.getCoursework(it) }
//            .subscribe({
//                Log.d("matej", "$it")
//            }, {
//                Log.e("matej", "$it")
//            })
    }
}