package sk.skwig.aisinator.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.feature.main.MainActivity
import sk.skwig.aisinator.feature.main.MainActivityViewModel

@Module(includes = [MainActivityViewModelModule::class, MainActivityActivityModule::class])
class MainActivityModule {

}

@Module
abstract class MainActivityViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel
}

@Module
abstract class MainActivityActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}