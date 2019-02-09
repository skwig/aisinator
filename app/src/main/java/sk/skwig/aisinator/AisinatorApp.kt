package sk.skwig.aisinator

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import sk.skwig.aisinator.di.AndroidModule
import sk.skwig.aisinator.di.AppComponent
import sk.skwig.aisinator.di.DaggerAppComponent
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
}
