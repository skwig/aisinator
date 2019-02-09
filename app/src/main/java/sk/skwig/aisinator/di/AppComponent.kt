package sk.skwig.aisinator.di

import dagger.Component
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.di.builder.ActivityBuilderModule
import sk.skwig.aisinator.di.builder.FragmentBuilderModule
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.course.di.CourseModule
import sk.skwig.aisinator.feature.settings.di.SettingsModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        PersistenceModule::class,

        AuthModule::class,
        CourseModule::class,
        SettingsModule::class,

        ActivityBuilderModule::class,
        FragmentBuilderModule::class
    ]
)
interface AppComponent {
    fun inject(application: AisinatorApp)
}