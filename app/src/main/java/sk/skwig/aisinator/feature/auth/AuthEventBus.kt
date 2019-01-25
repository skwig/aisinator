package sk.skwig.aisinator.feature.auth

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class AuthEventBus {

    private val eventRelay = PublishRelay.create<AuthenticationEvent>()

    val events: Observable<AuthenticationEvent>
        get() = eventRelay

    fun onAuthTimedOut() {
        eventRelay.accept(AuthenticationTimedOut)
    }
}


