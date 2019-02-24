package sk.skwig.aisinator.common.auth

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.common.settings.SettingsManager
import sk.skwig.aisinator.common.util.replayLast
import timber.log.Timber

const val COOKIE_HEADER = "Cookie"
private const val AUTH_HEADER = "UISAuth"

interface AuthManager {

    val authentication: Observable<Authentication>

    fun refreshAuth()
    fun login(login: String, password: String)

    data class LoginData(val login: String, val password: String) {
        val isComplete: Boolean
            get() = login.isNotBlank() && password.isNotBlank()
    }
}

class AuthManagerImpl(
    private val authMessageBus: AuthMessageBus,
    private val authApi: AuthApi,
    private val settingsManager: SettingsManager
) : AuthManager {

    override val authentication: Observable<Authentication>

    private val disposable = CompositeDisposable()

    private val loginDataRelay = BehaviorRelay.create<AuthManager.LoginData>()

    init {

        val credentialsFromSettings = Observable.just(Unit)
            .flatMap {
                AuthManager.LoginData(settingsManager.login, settingsManager.password).let {
                    if (it.isComplete) {
                        Observable.just(it)
                    } else {
                        authMessageBus.onLoginNeeded()
                        Observable.never()
                    }
                }
            }

        authentication = Observable.merge(loginDataRelay, credentialsFromSettings)
            .flatMapSingle { loginData ->
                authApi
                    .login(loginData.login, loginData.password)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        val headerValue = it.headers()[AUTH_HEADER]
                        if (headerValue != null) {
                            Single.just(Authentication(AUTH_HEADER, headerValue))
                        } else {
                            authMessageBus.onLoginNeeded()
                            Single.never()
                        }
                    }
                    .map { it to loginData }
            }
            .doOnNext {
                settingsManager.login = it.second.login
                settingsManager.password = it.second.password
            }
            .map { it.first }
            .replayLast()
            .refCount()
            .doOnNext {
                Log.d("matej", "Auth: [$it]")
            }

        disposable += authMessageBus.events
            .filter { it == AuthEvent.TimedOut }
            .subscribe({
                TODO("Handle auth time out (do reauth)")
            }, Timber::e)

    }

    override fun refreshAuth() {
        TODO()
    }

    override fun login(login: String, password: String) = loginDataRelay.accept(AuthManager.LoginData(login, password))

}