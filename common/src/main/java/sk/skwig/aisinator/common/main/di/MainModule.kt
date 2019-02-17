package sk.skwig.aisinator.common.main.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.common.main.MainFragment
import sk.skwig.aisinator.common.main.MainViewModel

@Module(includes = [MainViewModelModule::class, MainFragmentModule::class])
class MainModule {

}

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

}