package sk.skwig.aisinator.feature.auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.feature.auth.AuthApi
import sk.skwig.aisinator.feature.auth.AuthMessageBus
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.settings.SettingsManager
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(authMessageBus: AuthMessageBus, authApi: AuthApi, settingsManager: SettingsManager) = AuthManager(authMessageBus, authApi, settingsManager)

    @Provides
    @Singleton
    fun provideAuthEventBus() = AuthMessageBus()

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

}