package sk.skwig.aisinator.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.MainViewModel
import sk.skwig.aisinator.feature.dashboard.viewmodel.DashboardViewModel
import sk.skwig.aisinator.di.util.ViewModelFactory
import sk.skwig.aisinator.di.util.ViewModelKey
import sk.skwig.aisinator.feature.dashboard.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.feature.dashboard.viewmodel.CourseworkDeadlinesViewModel
import sk.skwig.aisinator.feature.login.viewmodel.LoginViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActiveCoursesViewModel::class)
    abstract fun bindActiveCoursesViewModel(activeCoursesViewModel: ActiveCoursesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseworkDeadlinesViewModel::class)
    abstract fun bindCourseworkDeadlinesViewModel(courseworkDeadlinesViewModel: CourseworkDeadlinesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}