package sk.skwig.aisinator.di

import dagger.Component
import dagger.Module
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.common.auth.di.AuthModule
import sk.skwig.aisinator.common.course.di.CourseModule
import sk.skwig.aisinator.common.deadline.di.DeadlineModule
import sk.skwig.aisinator.common.lesson.di.LessonModule
import sk.skwig.aisinator.common.settings.di.SettingsModule
import sk.skwig.aisinator.dashboard.di.DashboardModule
import sk.skwig.aisinator.feature.home.HomeModule
import sk.skwig.aisinator.login.di.LoginModule
import sk.skwig.aisinator.timetable.di.TimetableModule
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