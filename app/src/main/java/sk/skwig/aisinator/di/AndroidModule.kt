package sk.skwig.aisinator.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(val application: Application) {

    val applicationContext: Context by lazy {
        application
    }

    val resources: Resources by lazy {
        applicationContext.resources
    }
}
