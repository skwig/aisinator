package sk.skwig.aisinator.auth

data class Authentication(val authName: String, val authValue: String) {
    val cookie: String
        get() = "$authName=$authValue"
}