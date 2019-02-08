package sk.skwig.aisinator.common.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(val application: Application) {

    @Provides
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }
}
