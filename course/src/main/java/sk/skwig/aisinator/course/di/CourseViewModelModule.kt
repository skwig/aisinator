package sk.skwig.aisinator.course.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.course.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.course.viewmodel.CourseworkDeadlinesViewModel

@Module
abstract class CourseViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActiveCoursesViewModel::class)
    abstract fun bindActiveCoursesViewModel(activeCoursesViewModel: ActiveCoursesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseworkDeadlinesViewModel::class)
    abstract fun bindCourseworkDeadlinesViewModel(courseworkDeadlinesViewModel: CourseworkDeadlinesViewModel): ViewModel
}