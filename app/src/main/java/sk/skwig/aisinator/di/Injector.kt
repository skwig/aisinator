package sk.skwig.aisinator.di

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import sk.skwig.aisinator.common.deadline.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.common.lesson.viewmodel.UpcomingLessonsViewModel
import sk.skwig.aisinator.feature.home.HomeViewModel
import sk.skwig.aisinator.feature.main.MainActivityViewModel
import sk.skwig.aisinator.login.viewmodel.LoginViewModel
import sk.skwig.aisinator.timetable.TimetableViewModel

object Injector {

    lateinit var androidModule: AndroidModule
    lateinit var authModule: AuthModule
    lateinit var chatModule: ChatModule
    lateinit var courseModule: CourseModule
    lateinit var deadlineModule: DeadlineModule
    lateinit var lessonModule: LessonModule
    lateinit var networkModule: NetworkModule
    lateinit var persistenceModule: PersistenceModule
    lateinit var timetableModule: TimetableModule

    class SimpleFactory<out T : ViewModel?>(private val factory: () -> T) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory() as T
        }
    }

    fun injectDeadlineViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleFactory { DeadlinesViewModel(deadlineModule.deadlineRepository) })
            .get(DeadlinesViewModel::class.java)

    fun injectUpcomingLessonsViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleFactory { UpcomingLessonsViewModel(lessonModule.lessonRepository) })
            .get(UpcomingLessonsViewModel::class.java)

    fun injectLoginViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleFactory { LoginViewModel(authModule.authManager) })
            .get(LoginViewModel::class.java)

    fun injectTimetableViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleFactory { TimetableViewModel(lessonModule.lessonRepository) })
            .get(TimetableViewModel::class.java)

    fun injectHomeViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleFactory { HomeViewModel() })
            .get(HomeViewModel::class.java)

    fun injectMainActivityViewModel(activity: FragmentActivity) =
        ViewModelProviders.of(activity, SimpleFactory { MainActivityViewModel(authModule.authMessageBus) })
            .get(MainActivityViewModel::class.java)

}