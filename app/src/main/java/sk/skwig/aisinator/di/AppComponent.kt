package sk.skwig.aisinator.di

import dagger.Component
import sk.skwig.aisinator.AisinatorApp
import sk.skwig.aisinator.di.builder.ActivityBuilderModule
import sk.skwig.aisinator.di.builder.FragmentBuilderModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        ViewModelModule::class,
        NetworkModule::class,

        ActivityBuilderModule::class,
        FragmentBuilderModule::class
    ]
)
interface AppComponent {
    fun inject(application: AisinatorApp)
}