package sk.skwig.aisinator.di

import dagger.Component
import dagger.Module
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.feature.home.HomeModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        MainActivityModule::class,

        ViewModelFactoryModule::class,

        LibraryModule::class,
        FeatureModule::class
    ]
)
interface AppComponent {
    fun inject(application: AisinatorApp)
}


@Module(
    includes = [
        HomeModule::class,
        DashboardModule::class,
        LoginModule::class,
        TimetableModule::class
    ]
)
class FeatureModule

@Module(
    includes = [
        NetworkModule::class,
        PersistenceModule::class,

        AuthModule::class,
        SettingsModule::class,
        CourseModule::class,
        DeadlineModule::class,
        LessonModule::class
    ]
)
class LibraryModule