package sk.skwig.aisinator.common.auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.common.auth.AuthApi
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.auth.AuthManagerImpl
import sk.skwig.aisinator.common.auth.AuthMessageBus
import sk.skwig.aisinator.common.settings.SettingsManager
import javax.inject.Named
import javax.inject.Singleton

const val AIS = "ais"
const val AIS_AUTH = "ais_auth"
const val DASHBOARD = "dashboard"

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(
        authMessageBus: AuthMessageBus,
        authApi: AuthApi,
        settingsManager: SettingsManager
    ): AuthManager = AuthManagerImpl(authMessageBus, authApi, settingsManager)

    @Provides
    @Singleton
    fun provideAuthEventBus() = AuthMessageBus()

    @Singleton
    @Provides
    fun provideAuthApi(@Named(AIS) retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

}