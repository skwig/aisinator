package sk.skwig.aisinator.feature.course.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.common.AppDatabase
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.course.CourseApi
import sk.skwig.aisinator.feature.course.CourseHtmlParser
import sk.skwig.aisinator.feature.course.CourseRepository
import sk.skwig.aisinator.feature.course.CourseRepositoryImpl
import sk.skwig.aisinator.feature.course.db.*
import sk.skwig.aisinator.feature.course.db.roomdao.CourseRoomDao
import sk.skwig.aisinator.feature.course.db.roomdao.CourseworkDeadlineRoomDao
import javax.inject.Singleton

@Module
class CourseModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        authManager: AuthManager,
        courseApi: CourseApi,
        courseHtmlParser: CourseHtmlParser,
        courseDao: CourseDao,
        courseworkDeadlineDao: CourseworkDeadlineDao
    ): CourseRepository = CourseRepositoryImpl(
        authManager,
        courseApi,
        courseHtmlParser,
        courseDao,
        courseworkDeadlineDao
    )

    @Provides
    @Singleton
    fun provideCourseHtmlParser() = CourseHtmlParser()

    @Singleton
    @Provides
    fun provideCourseApi(retrofit: Retrofit) = retrofit.create(CourseApi::class.java)

    @Singleton
    @Provides
    fun provideCourseRoomDao(appDatabase: AppDatabase) = appDatabase.courseDao()

    @Singleton
    @Provides
    fun provideCourseDao(courseRoomDao: CourseRoomDao, courseMapper: CourseMapper): CourseDao =
        CourseDaoImpl(courseRoomDao, courseMapper)

    @Singleton
    @Provides
    fun provideCourseworkDeadlineRoomDao(appDatabase: AppDatabase) = appDatabase.courseworkDeadlineDao()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineDao(
        courseworkDeadlineRoomDao: CourseworkDeadlineRoomDao,
        courseMapper: CourseMapper,
        courseworkDeadlineMapper: CourseworkDeadlineMapper,
        courseworkDeadlineWithCourseMapper: CourseworkDeadlineWithCourseMapper
    ): CourseworkDeadlineDao =
        CourseworkDeadlineDaoImpl(
            courseworkDeadlineRoomDao,
            courseMapper,
            courseworkDeadlineMapper,
            courseworkDeadlineWithCourseMapper
        )

    @Singleton
    @Provides
    fun provideCourseMapper() = CourseMapper()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineMapper() = CourseworkDeadlineMapper()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineWithCourseMapper(
        courseMapper: CourseMapper,
        courseworkDeadlineMapper: CourseworkDeadlineMapper
    ) =
        CourseworkDeadlineWithCourseMapper(courseMapper, courseworkDeadlineMapper)
}