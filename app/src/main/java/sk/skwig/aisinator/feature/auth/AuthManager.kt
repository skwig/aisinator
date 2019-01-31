package sk.skwig.aisinator.feature.auth

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.feature.settings.SettingsManager
import timber.log.Timber

private const val AUTH_HEADER = "UISAuth"

class AuthManager(
    private val authMessageBus: AuthMessageBus,
    private val authApi: AuthApi,
    private val settingsManager: SettingsManager
) {

    private val disposable = CompositeDisposable()

    private val foo = PublishRelay.create<Unit>()

    private val loginDataRelay = BehaviorRelay.create<LoginData>()

    val authentication: Observable<Authentication>

    init {

        val settingsFoo = Observable.just(Unit)
            .flatMap {
                if (settingsManager.hasLoginCredentials) {
                    Observable.just(LoginData(settingsManager.login, settingsManager.password))
                } else {
                    authMessageBus.onLoginNeeded()
                    Observable.never()
                }
            }

        authentication = Observable.merge(loginDataRelay, settingsFoo)
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
            .replay(1)
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

    fun refreshAuth() {
        TODO()
    }

    fun login(login: String, password: String) = loginDataRelay.accept(LoginData(login, password))

    data class LoginData(val login: String, val password: String)

}