package sk.skwig.aisinator.feature.auth.di

import sk.skwig.aisinator.di.NetworkModule
import sk.skwig.aisinator.feature.settings.di.SettingsModule
import sk.skwig.aisinator.feature.auth.AuthApi
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.auth.AuthManagerImpl
import sk.skwig.aisinator.feature.auth.AuthMessageBus
import javax.inject.Singleton

const val AIS = "ais"
const val AIS_AUTH = "ais_auth"
const val DASHBOARD = "dashboard"

class AuthModule {

    lateinit var networkModule: NetworkModule
    lateinit var settingsModule : SettingsModule

    @Singleton
    val authManager: AuthManager by lazy {
        AuthManagerImpl(
            authMessageBus,
            authApi,
            settingsModule.settingsManager
        )
    }

    @Singleton
    val authMessageBus by lazy { AuthMessageBus() }

    @Singleton
    val authApi by lazy { networkModule.aisRetrofit.create(AuthApi::class.java)}

}