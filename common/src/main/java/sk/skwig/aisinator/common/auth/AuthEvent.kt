package sk.skwig.aisinator.common.auth

sealed class AuthEvent {

    object TimedOut : AuthEvent()
    object LoginNeeded : AuthEvent()

}
