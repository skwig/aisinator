package sk.skwig.aisinator.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.chat.di.ChatModule
import sk.skwig.aisinator.feature.chat.viewmodel.ChatroomListViewModel
import sk.skwig.aisinator.feature.chat.viewmodel.ChatroomViewModel
import sk.skwig.aisinator.feature.course.di.CourseModule
import sk.skwig.aisinator.feature.dashboard.viewmodel.DashboardViewModel
import sk.skwig.aisinator.feature.deadline.di.DeadlineModule
import sk.skwig.aisinator.feature.deadline.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.feature.home.HomeViewModel
import sk.skwig.aisinator.feature.lesson.di.LessonModule
import sk.skwig.aisinator.feature.lesson.viewmodel.UpcomingLessonsViewModel
import sk.skwig.aisinator.feature.login.viewmodel.LoginViewModel
import sk.skwig.aisinator.feature.main.MainActivityViewModel
import sk.skwig.aisinator.feature.settings.di.SettingsModule
import sk.skwig.aisinator.feature.timetable.TimetableViewModel
import sk.skwig.aisinator.feature.timetable.di.TimetableModule

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
    lateinit var settingsModule: SettingsModule

    private class SimpleViewModelFactory<out T : ViewModel?>(private val factory: () -> T) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = factory() as T
    }

    fun injectDeadlineViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory {
            DeadlinesViewModel(
                deadlineModule.deadlineRepository
            )
        })
            .get(DeadlinesViewModel::class.java)

    fun injectUpcomingLessonsViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory {
            UpcomingLessonsViewModel(
                lessonModule.lessonRepository
            )
        })
            .get(UpcomingLessonsViewModel::class.java)

    fun injectLoginViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { LoginViewModel(authModule.authManager) })
            .get(LoginViewModel::class.java)

    fun injectTimetableViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { TimetableViewModel(lessonModule.lessonRepository, settingsModule.settingsManager) })
            .get(TimetableViewModel::class.java)

    fun injectHomeViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { HomeViewModel() })
            .get(HomeViewModel::class.java)

    fun injectMainActivityViewModel(activity: FragmentActivity) =
        ViewModelProviders.of(activity, SimpleViewModelFactory { MainActivityViewModel(authModule.authMessageBus) })
            .get(MainActivityViewModel::class.java)

    fun injectDashboardViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { DashboardViewModel() })
            .get(DashboardViewModel::class.java)

    fun injectChatroomListViewModel(fragment: Fragment) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { ChatroomListViewModel(courseModule.courseRepository) })
            .get(ChatroomListViewModel::class.java)

    fun injectChatroomViewModel(fragment: Fragment, chatroomId: Long) =
        ViewModelProviders.of(fragment, SimpleViewModelFactory { ChatroomViewModel(chatroomId, chatModule.chatPager) })
            .get(ChatroomViewModel::class.java)

}