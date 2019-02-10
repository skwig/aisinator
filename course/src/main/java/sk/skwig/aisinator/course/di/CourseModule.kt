package sk.skwig.aisinator.course.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.auth.AuthManager
import sk.skwig.aisinator.course.CourseDaoProvider
import sk.skwig.aisinator.course.CourseRepository
import sk.skwig.aisinator.course.CourseRepositoryImpl
import sk.skwig.aisinator.course.api.CourseApi
import sk.skwig.aisinator.course.api.CourseHtmlParser
import sk.skwig.aisinator.course.db.*
import sk.skwig.aisinator.course.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.course.db.roomdao.DeadlineRoomDao
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
    fun provideCourseRoomDao(courseDaoProvider: CourseDaoProvider) = courseDaoProvider.courseDao()

    @Singleton
    @Provides
    fun provideCourseDao(courseRoomDao: CourseRoomDao, courseMapper: CourseMapper): CourseDao =
        CourseDaoImpl(courseRoomDao, courseMapper)

    @Singleton
    @Provides
    fun provideCourseworkDeadlineRoomDao(courseDaoProvider: CourseDaoProvider) =
        courseDaoProvider.courseworkDeadlineDao()

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