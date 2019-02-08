package sk.skwig.aisinator.common.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.skwig.aisinator.MainActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}