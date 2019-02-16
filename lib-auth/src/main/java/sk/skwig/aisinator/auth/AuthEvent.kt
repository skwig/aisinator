package sk.skwig.aisinator.auth

sealed class AuthEvent {

    object TimedOut : AuthEvent()
    object LoginNeeded : AuthEvent()

}
