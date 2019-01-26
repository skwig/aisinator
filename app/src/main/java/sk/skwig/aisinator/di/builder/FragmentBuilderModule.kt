package sk.skwig.aisinator.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.skwig.aisinator.feature.login.LoginFragment
import sk.skwig.aisinator.feature.main.DashboardFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeReservationFragment(): ReservationFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeIntroSettingsFragment(): IntroSettingsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeIntroDatePickerFragment(): DatePickerFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeSearchFragment(): SearchFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeStationUpdateFragment(): StationUpdateDialogFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeRouteResultFragment(): RouteListingFragment
}