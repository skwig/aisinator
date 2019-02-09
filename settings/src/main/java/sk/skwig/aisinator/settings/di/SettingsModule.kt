package sk.skwig.aisinator.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.settings.SettingsManager
import javax.inject.Singleton

@Module
class SettingsModule {

    @Provides
    @Singleton
    fun privideSettingsManager(context: Context) = SettingsManager(context)

}