package sk.skwig.aisinator.dashboard.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.dashboard.LessonDaoProvider
import sk.skwig.aisinator.dashboard.LessonRepository
import sk.skwig.aisinator.dashboard.LessonRepositoryImpl
import sk.skwig.aisinator.dashboard.api.*
import sk.skwig.aisinator.dashboard.db.*
import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao
import sk.skwig.aisinator.dashboard.viewmodel.UpcomingLessonsViewModel
import javax.inject.Singleton

@Module(includes = [LessonViewModelModule::class])
class LessonModule {

    @Provides
    @Singleton
    fun provideLessonRepository(
        authManager: AuthManager,
        lessonApi: LessonApi,
        lessonDao: LessonDao
    ): LessonRepository =
        LessonRepositoryImpl(
            authManager,
            lessonApi,
            lessonDao
        )

    @Provides
    @Singleton
    fun provideLessonHtmlParser(): LessonHtmlParser = LessonHtmlParserImpl()

    @Singleton
    @Provides
    fun provideLessonRetrofitApi(retrofit: Retrofit): LessonRetrofitApi = retrofit.create(LessonRetrofitApi::class.java)

    @Singleton
    @Provides
    fun provideDeadlineApi(lessonRetrofitApi: LessonRetrofitApi, lessonHtmlParser: LessonHtmlParser): LessonApi =
        LessonApiImpl(lessonRetrofitApi, lessonHtmlParser)

    @Singleton
    @Provides
    fun provideLessonRoomDao(lessonDaoProvider: LessonDaoProvider): LessonRoomDao =
        lessonDaoProvider.lessonDao()


    @Singleton
    @Provides
    fun provideLessonDao(
        lessonRoomDao: LessonRoomDao,
        courseMapper: CourseMapper,
        lessonMapper: LessonMapper,
        upcomingLessonWithCourseMapper: UpcomingLessonWithCourseMapper
    ): LessonDao =
        LessonDaoImpl(
            lessonRoomDao,
            courseMapper,
            lessonMapper,
            upcomingLessonWithCourseMapper
        )


    @Singleton
    @Provides
    fun provideLessonMapper(lessonTimeMapper: LessonTimeMapper) = LessonMapper(lessonTimeMapper)

    @Singleton
    @Provides
    fun provideLessonTimeMapper() = LessonTimeMapper()

    @Singleton
    @Provides
    fun provideUpcomingLessonWithCourseMapper(
        courseMapper: CourseMapper,
        lessonMapper: LessonMapper,
        lessonTimeMapper: LessonTimeMapper
    ) = UpcomingLessonWithCourseMapper(courseMapper, lessonMapper, lessonTimeMapper)
}

@Module
abstract class LessonViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingLessonsViewModel::class)
    abstract fun bindUpcomingLessonsViewModel(upcomingLessonsViewModel: UpcomingLessonsViewModel): ViewModel
}