package sk.skwig.aisinator.common.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.common.settings.SettingsManager
import sk.skwig.aisinator.common.settings.SettingsManagerImpl
import javax.inject.Singleton

@Module
class SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsManager(context: Context): SettingsManager = SettingsManagerImpl(context)

}
