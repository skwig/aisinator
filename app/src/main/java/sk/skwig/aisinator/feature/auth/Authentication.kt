package sk.skwig.aisinator.feature.auth

data class Authentication(val authName: String, val authValue: String) {
    val cookie: String
        get() = "$authName=$authValue"
}