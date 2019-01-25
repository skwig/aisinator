package sk.skwig.aisinator.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.auth.AuthManager
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    private val stateRelay = BehaviorRelay.createDefault(State.INITIAL)

    val state: Observable<State>
        get() = stateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += authManager.authentication
            .map { State.SUCCESS }
            .onErrorReturnItem(State.ERROR)
            .subscribe(stateRelay)
    }

    fun onLogin(login: String, password: String) {
        if (login.isNotBlank() && password.isNotBlank()) {
            stateRelay.accept(State.LOADING)
            authManager.login(login, password)
        } else {
            stateRelay.accept(State.ERROR)
        }
    }

    enum class State { INITIAL, LOADING, SUCCESS, ERROR }
}