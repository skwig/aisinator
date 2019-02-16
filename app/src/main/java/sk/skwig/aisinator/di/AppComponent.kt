package sk.skwig.aisinator.di

import dagger.Component
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.common.auth.di.AuthModule
import sk.skwig.aisinator.dashboard.di.CourseModule
import sk.skwig.aisinator.dashboard.di.DashboardModule
import sk.skwig.aisinator.login.di.LoginModule
import sk.skwig.aisinator.common.settings.di.SettingsModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        ActivityBuilderModule::class,

        NetworkModule::class,
        PersistenceModule::class,

        AuthModule::class,
        CourseModule::class,
        DashboardModule::class,
        LoginModule::class,
        SettingsModule::class,

        // TODO: asap do feature modulu
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(application: AisinatorApp)
}