package sk.skwig.aisinator.feature.settings.di

import sk.skwig.aisinator.di.AndroidModule
import sk.skwig.aisinator.feature.settings.SettingsManager
import sk.skwig.aisinator.feature.settings.SettingsManagerImpl
import javax.inject.Singleton

class SettingsModule {

    lateinit var androidModule: AndroidModule

    @Singleton
    val settingsManager: SettingsManager by lazy {
        SettingsManagerImpl(
            androidModule.applicationContext
        )
    }

}
