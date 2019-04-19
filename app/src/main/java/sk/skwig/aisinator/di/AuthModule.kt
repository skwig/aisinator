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

    @Singleton
    val provideAuthManager: AuthManager by lazy { AuthManagerImpl(authMessageBus, authApi, settingsManager) }

    @Singleton
    val authEventBus by lazy { AuthMessageBus() }

    @Singleton
    val authApi by lazy { aisRetrofit.create(AuthApi::class.java)}

}