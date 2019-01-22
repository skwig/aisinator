package sk.skwig.aisinator.di

import dagger.Component
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.di.builder.ActivityBuilderModule
import sk.skwig.aisinator.di.builder.FragmentBuilderModule
import sk.skwig.aisinator.feature.authentication.AuthenticationManager
import sk.skwig.aisinator.feature.authentication.di.AuthenticationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        ViewModelModule::class,
        NetworkModule::class,

        TempModule::class,
        AuthenticationModule::class,

        ActivityBuilderModule::class,
        FragmentBuilderModule::class
    ]
)
interface AppComponent {
    fun inject(application: AisinatorApp)
}