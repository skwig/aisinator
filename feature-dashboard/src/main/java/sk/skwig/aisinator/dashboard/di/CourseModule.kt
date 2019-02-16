package sk.skwig.aisinator.dashboard.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.dashboard.DashboardDaoProvider
import sk.skwig.aisinator.dashboard.DashboardRepository
import sk.skwig.aisinator.dashboard.DashboardRepositoryImpl
import sk.skwig.aisinator.dashboard.api.DashboardApi
import sk.skwig.aisinator.dashboard.api.DashboardHtmlParser
import sk.skwig.aisinator.dashboard.api.DashboardRetrofitApi
import sk.skwig.aisinator.dashboard.db.*
import sk.skwig.aisinator.dashboard.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao
import javax.inject.Singleton

@Module(includes = [CourseViewModelModule::class])
class CourseModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        authManager: AuthManager,
        dashboardApi: DashboardApi,
        courseDao: CourseDao,
        deadlineDao: DeadlineDao,
        lessonDao: LessonDao
    ): DashboardRepository = DashboardRepositoryImpl(
        authManager,
        dashboardApi,
        courseDao,
        deadlineDao,
        lessonDao
    )

    @Provides
    @Singleton
    fun provideCourseHtmlParser() = DashboardHtmlParser()

    @Singleton
    @Provides
    fun provideDashboardRetrofitApi(retrofit: Retrofit) = retrofit.create(DashboardRetrofitApi::class.java)

    @Singleton
    @Provides
    fun provideDashboardApi(dashboardRetrofitApi: DashboardRetrofitApi, dashboardHtmlParser: DashboardHtmlParser) =
        DashboardApi(dashboardRetrofitApi, dashboardHtmlParser)

    @Singleton
    @Provides
    fun provideCourseRoomDao(dashboardDaoProvider: DashboardDaoProvider) = dashboardDaoProvider.courseDao()

    @Singleton
    @Provides
    fun provideCourseDao(courseRoomDao: CourseRoomDao, courseMapper: CourseMapper): CourseDao =
        CourseDaoImpl(courseRoomDao, courseMapper)

    @Singleton
    @Provides
    fun provideDeadlineRoomDao(dashboardDaoProvider: DashboardDaoProvider) =
        dashboardDaoProvider.deadlineDao()

    @Singleton
    @Provides
    fun provideLessonRoomDao(dashboardDaoProvider: DashboardDaoProvider) =
        dashboardDaoProvider.lessonDao()

    @Singleton
    @Provides
    fun provideDeadlineDao(
        deadlineRoomDao: DeadlineRoomDao,
        courseMapper: CourseMapper,
        deadlineMapper: DeadlineMapper,
        deadlineWithCourseMapper: DeadlineWithCourseMapper
    ): DeadlineDao =
        DeadlineDaoImpl(
            deadlineRoomDao,
            courseMapper,
            deadlineMapper,
            deadlineWithCourseMapper
        )

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
    fun provideCourseMapper() = CourseMapper()

    @Singleton
    @Provides
    fun provideDeadlineMapper() = DeadlineMapper()

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

    @Singleton
    @Provides
    fun provideLessonMapper(lessonTimeMapper: LessonTimeMapper) = LessonMapper(lessonTimeMapper)

    @Singleton
    @Provides
    fun provideDeadlineWithCourseMapper(
        courseMapper: CourseMapper,
        deadlineMapper: DeadlineMapper
    ) =
        DeadlineWithCourseMapper(courseMapper, deadlineMapper)
}