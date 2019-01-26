package sk.skwig.aisinator.feature.auth

sealed class AuthEvent {

    object TimedOut : AuthEvent()
    object LoginNeeded : AuthEvent()

}
