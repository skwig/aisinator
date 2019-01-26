package sk.skwig.aisinator.feature.auth

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class AuthEventBus {

    private val eventRelay = PublishRelay.create<AuthEvent>()

    val events: Observable<AuthEvent>
        get() = eventRelay

    fun onAuthTimedOut() {
        eventRelay.accept(AuthEvent.TimedOut)
    }

    fun onLoginNeeded() {
        eventRelay.accept(AuthEvent.LoginNeeded)
    }
}


