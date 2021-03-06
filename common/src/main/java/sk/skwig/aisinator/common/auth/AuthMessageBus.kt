package sk.skwig.aisinator.common.auth

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class AuthMessageBus {

    private val eventRelay = PublishRelay.create<AuthEvent>()

    val events: Observable<AuthEvent>
        get() = eventRelay

    fun onAuthTimedOut() {
        Log.d("matej","onAuthTimedOut() called")
        eventRelay.accept(AuthEvent.TimedOut)
    }

    fun onLoginNeeded() {
        Log.d("matej","onLoginNeeded() called")
        eventRelay.accept(AuthEvent.LoginNeeded)
    }
}


