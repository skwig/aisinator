package sk.skwig.aisinator.timetable.di


import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.timetable.TimetableFragment
import sk.skwig.aisinator.timetable.TimetableViewModel

@Module(includes = [TimetableViewModelModule::class, TimetableFragmentModule::class])
class TimetableModule {

}

@Module
abstract class TimetableViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TimetableViewModel::class)
    abstract fun bindTimetableViewModel(timetableViewModel: TimetableViewModel): ViewModel
}

@Module
abstract class TimetableFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeTimetableFragment(): TimetableFragment

}