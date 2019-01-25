package sk.skwig.aisinator.feature.auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.feature.auth.AuthApi
import sk.skwig.aisinator.feature.auth.AuthEventBus
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.settings.SettingsManager
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(authEventBus: AuthEventBus, authApi: AuthApi, settingsManager: SettingsManager) = AuthManager(authEventBus, authApi, settingsManager)

    @Provides
    @Singleton
    fun provideAuthEventBus() = AuthEventBus()

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

}