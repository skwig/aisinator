package sk.skwig.aisinator.login.di

import dagger.Module

@Module(includes = [LoginViewModelModule::class, LoginFragmentModule::class])
class LoginModule {
}