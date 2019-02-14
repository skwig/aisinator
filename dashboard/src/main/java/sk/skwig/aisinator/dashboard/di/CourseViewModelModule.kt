package sk.skwig.aisinator.dashboard.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.dashboard.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.dashboard.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.dashboard.viewmodel.UpcomingLessonsViewModel

@Module
abstract class CourseViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActiveCoursesViewModel::class)
    abstract fun bindActiveCoursesViewModel(activeCoursesViewModel: ActiveCoursesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeadlinesViewModel::class)
    abstract fun bindCourseworkDeadlinesViewModel(deadlinesViewModel: DeadlinesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingLessonsViewModel::class)
    abstract fun bindUpcomingLessonsViewModel(upcomingLessonsViewModel: UpcomingLessonsViewModel): ViewModel
}