package sk.skwig.aisinator.feature.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.auth.AuthManager
import sk.skwig.aisinator.auth.AuthMessageBus
import sk.skwig.aisinator.course.CourseRepository
import sk.skwig.aisinator.settings.SettingsManager
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val authMessageBus: AuthMessageBus,
    private val courseRepository: CourseRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val showLoginRelay = PublishRelay.create<Unit>()

//    val showLogin: Observable<Unit>

    init {
        Log.d("matej","DashboardViewModel INIT")
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

//        Observable.timer(1, TimeUnit.SECONDS).subscribe {

//            disposable += courseRepository.getActiveCourses()
//                .map { it.first() }
//                .flatMap { courseRepository.getCoursework(it) }
//                .subscribe({
//                    Log.d("matej", "$it")
//                }, {
//                    Log.e("matej", "$it")
//                })
//        }
    }
}