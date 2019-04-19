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
class AisinatorApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    companion object {
        val LOG_TAG_PREFIX = "ais_"
    }

    var component: AppComponent? = null
        private set

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, LOG_TAG_PREFIX + tag!!, message, t)
                }
            })
        }

        component = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .build()
            .also {
                it.inject(this)
            }
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    fun constructInjector() : Injector{
        return Injector().apply {
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
            deadlineModule.courseModule = courseModule
            deadlineModule.networkModule = networkModule

            lessonModule.authModule = authModule
            lessonModule.courseModule = courseModule
            lessonModule.networkModule = networkModule

            persistenceModule.androidModule = androidModule
        }
    }
}
