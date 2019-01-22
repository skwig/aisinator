package sk.skwig.aisinator.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.common.AisApi
import javax.inject.Singleton

@Module
class TempModule {
    @Singleton
    @Provides
    fun provideAisApi(retrofit: Retrofit) = retrofit.create(AisApi::class.java)
}