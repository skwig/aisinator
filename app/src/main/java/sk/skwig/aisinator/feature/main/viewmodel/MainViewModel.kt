package sk.skwig.aisinator.feature.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.authentication.AuthenticationManager
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val authenticationManager: AuthenticationManager) : ViewModel() {

    private val disposable = CompositeDisposable()

    init {
//        disposable += authenticationManager.foo
//            .subscribe { it ->
//                Log.d("matej", "$it")
//            }
    }

    fun doFoo() {
        disposable += authenticationManager.foo
            .subscribe({ it ->
                Log.d("matej", "$it")
            }, Timber::e)
    }

}