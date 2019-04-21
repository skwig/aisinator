package sk.skwig.aisinator

import android.app.Application
import sk.skwig.aisinator.di.*
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.chat.di.ChatModule
import sk.skwig.aisinator.feature.course.di.CourseModule
import sk.skwig.aisinator.feature.deadline.di.DeadlineModule
import sk.skwig.aisinator.feature.lesson.di.LessonModule
import sk.skwig.aisinator.feature.settings.di.SettingsModule
import sk.skwig.aisinator.feature.timetable.di.TimetableModule
import timber.log.Timber

/**
 * Created by Matej on 29. 7. 2016.
 */
class AisinatorApp : Application() {

    companion object {
        val LOG_TAG_PREFIX = "ais_"
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, LOG_TAG_PREFIX + tag!!, message, t)
                }
            })
        }

        setupInjectorGraph()
    }

    fun setupInjectorGraph() : Injector{
        return Injector.apply {
            androidModule = AndroidModule(this@AisinatorApp)
            authModule = AuthModule()
            chatModule = ChatModule()
            courseModule = CourseModule()
            deadlineModule = DeadlineModule()
            lessonModule = LessonModule()
            persistenceModule = PersistenceModule()
            timetableModule = TimetableModule()
            networkModule = NetworkModule()
            settingsModule = SettingsModule()

            authModule.networkModule = networkModule
            authModule.settingsModule = settingsModule

            courseModule.authModule = authModule
            courseModule.networkModule = networkModule
            courseModule.persistenceModule = persistenceModule

            deadlineModule.authModule = authModule
            deadlineModule.networkModule = networkModule
            deadlineModule.persistenceModule = persistenceModule
            deadlineModule.courseModule = courseModule

            lessonModule.authModule = authModule
            lessonModule.networkModule = networkModule
            lessonModule.persistenceModule = persistenceModule
            lessonModule.courseModule = courseModule

            persistenceModule.androidModule = androidModule

            networkModule.authModule = authModule

            settingsModule.androidModule = androidModule
        }
    }
}
