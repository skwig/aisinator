package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.settings.SettingsManager
import sk.skwig.aisinator.common.settings.SettingsManagerImpl
import javax.inject.Singleton

class SettingsModule {

    lateinit var androidModule: AndroidModule

    @Singleton
    val settingsManager: SettingsManager by lazy { SettingsManagerImpl(androidModule.applicationContext) }

}
