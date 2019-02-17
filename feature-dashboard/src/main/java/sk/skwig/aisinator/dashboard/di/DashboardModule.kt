package sk.skwig.aisinator.dashboard.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.dashboard.DashboardFragment
import sk.skwig.aisinator.dashboard.viewmodel.DashboardViewModel

@Module(includes = [DashboardViewModelModule::class, DashboardFragmentModule::class])
class DashboardModule {

}

@Module
abstract class DashboardViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel
}

@Module
abstract class DashboardFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

}