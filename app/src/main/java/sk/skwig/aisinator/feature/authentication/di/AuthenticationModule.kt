package sk.skwig.aisinator.feature.authentication.di

import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.common.AisApi
import sk.skwig.aisinator.feature.authentication.AuthenticationManager
import javax.inject.Singleton

@Module
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationManager(aisApi: AisApi) = AuthenticationManager(aisApi)

}