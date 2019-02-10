package sk.skwig.aisinator.dashboard.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.auth.AuthManager
import sk.skwig.aisinator.dashboard.DashboardDaoProvider
import sk.skwig.aisinator.dashboard.CourseRepository
import sk.skwig.aisinator.dashboard.CourseRepositoryImpl
import sk.skwig.aisinator.dashboard.api.CourseApi
import sk.skwig.aisinator.dashboard.api.CourseHtmlParser
import sk.skwig.aisinator.dashboard.db.*
import sk.skwig.aisinator.dashboard.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao
import javax.inject.Singleton

@Module(includes = [CourseViewModelModule::class])
class CourseModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        authManager: AuthManager,
        courseApi: CourseApi,
        courseHtmlParser: CourseHtmlParser,
        courseDao: CourseDao,
        deadlineDao: DeadlineDao
    ): CourseRepository = CourseRepositoryImpl(
        authManager,
        courseApi,
        courseHtmlParser,
        courseDao,
        deadlineDao
    )

    @Provides
    @Singleton
    fun provideCourseHtmlParser() = CourseHtmlParser()

    @Singleton
    @Provides
    fun provideCourseApi(retrofit: Retrofit) = retrofit.create(CourseApi::class.java)

    @Singleton
    @Provides
    fun provideCourseRoomDao(dashboardDaoProvider: DashboardDaoProvider) = dashboardDaoProvider.courseDao()

    @Singleton
    @Provides
    fun provideCourseDao(courseRoomDao: CourseRoomDao, courseMapper: CourseMapper): CourseDao =
        CourseDaoImpl(courseRoomDao, courseMapper)

    @Singleton
    @Provides
    fun provideCourseworkDeadlineRoomDao(dashboardDaoProvider: DashboardDaoProvider) =
        dashboardDaoProvider.deadlineDao()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineDao(
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
    fun provideCourseMapper() = CourseMapper()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineMapper() = DeadlineMapper()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineWithCourseMapper(
        courseMapper: CourseMapper,
        deadlineMapper: DeadlineMapper
    ) =
        DeadlineWithCourseMapper(courseMapper, deadlineMapper)
}