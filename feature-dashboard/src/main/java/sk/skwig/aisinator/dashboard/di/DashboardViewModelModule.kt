package sk.skwig.aisinator.dashboard.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.dashboard.viewmodel.DashboardViewModel

@Module
abstract class DashboardViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel
}