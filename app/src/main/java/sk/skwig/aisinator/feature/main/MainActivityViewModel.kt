package sk.skwig.aisinator.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import sk.skwig.aisinator.common.auth.AuthEvent
import sk.skwig.aisinator.common.auth.AuthMessageBus
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(authMessageBus: AuthMessageBus) : ViewModel() {

    val showLoginScreen: Observable<Unit>

    init {
        Log.d("matej","MainViewModel INIT")

        showLoginScreen = authMessageBus.events
            .filter { it is AuthEvent.LoginNeeded }
            .map { Unit }
            .doOnSubscribe {
                Log.d("matej", "showLogin.doOnSubscribe ()")
            }
    }

}