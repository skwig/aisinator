package sk.skwig.aisinator.feature.auth

class AuthTimedOutException : RuntimeException("Auth timed out")
class AuthUserLoginNeededException : RuntimeException("Auth timed out")