package sk.skwig.aisinator

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import sk.skwig.aisinator.di.*
import timber.log.Timber
import javax.inject.Inject

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
    }

    fun constructInjector() : Injector{
        return Injector.apply {
            androidModule = AndroidModule(this@AisinatorApp)
            authModule = AuthModule()
            chatModule = ChatModule()
            courseModule = CourseModule()
            deadlineModule = DeadlineModule()
            lessonModule = LessonModule()
            persistenceModule = PersistenceModule()
            timetableModule = TimetableModule()

            authModule.networkModule = networkModule

            courseModule.authModule = authModule
            courseModule.networkModule = networkModule

            deadlineModule.authModule = authModule
            deadlineModule.networkModule = networkModule
            deadlineModule.courseModule = courseModule

            lessonModule.authModule = authModule
            lessonModule.networkModule = networkModule
            lessonModule.courseModule = courseModule

            persistenceModule.androidModule = androidModule
        }
    }
}
