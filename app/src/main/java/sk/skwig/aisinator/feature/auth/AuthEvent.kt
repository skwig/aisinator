package sk.skwig.aisinator.feature.auth

sealed class AuthenticationEvent

object AuthenticationTimedOut : AuthenticationEvent()