package sk.skwig.aisinator.dashboard.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.skwig.aisinator.dashboard.DashboardFragment

@Module
abstract class DashboardFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

}