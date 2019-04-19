package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.auth.AuthApi
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.auth.AuthManagerImpl
import sk.skwig.aisinator.common.auth.AuthMessageBus
import javax.inject.Singleton

const val AIS = "ais"
const val AIS_AUTH = "ais_auth"
const val DASHBOARD = "dashboard"

class AuthModule {

    lateinit var networkModule: NetworkModule
    lateinit var settingsModule : SettingsModule

    @Singleton
    val authManager: AuthManager by lazy { AuthManagerImpl(authMessageBus, authApi, settingsModule.settingsManager) }

    @Singleton
    val authMessageBus by lazy { AuthMessageBus() }

    @Singleton
    val authApi by lazy { networkModule.aisRetrofit.create(AuthApi::class.java)}

}