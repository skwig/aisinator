package sk.skwig.aisinator.login.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.skwig.aisinator.login.LoginFragment

@Module
abstract class LoginFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

}