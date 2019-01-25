package sk.skwig.aisinator.feature.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.course.CourseRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val authManager: AuthManager, private val courseRepository: CourseRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    init {
//        disposable += authenticationManager.onAuthTimedOut
//            .subscribe { it ->
//                Log.d("matej", "$it")
//            }

        disposable += courseRepository.getActiveCourses()
            .map { it.first() }
            .flatMap { courseRepository.getCoursework(it) }
            .subscribe({
                Log.d("matej", "$it")
            }, {
                Log.e("matej", "$it")
            })
    }
}