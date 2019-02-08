package sk.skwig.aisinator.feature.course.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.common.AppDatabase
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.course.*
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
        courseMapper: CourseMapper,
        courseworkDeadlineMapper: CourseworkDeadlineMapper
    ): CourseRepository = CourseRepositoryImpl(
        authManager,
        courseApi,
        courseHtmlParser,
        courseDao,
        courseMapper,
        courseworkDeadlineMapper
    )

    @Provides
    @Singleton
    fun provideCourseHtmlParser() = CourseHtmlParser()

    @Singleton
    @Provides
    fun provideCourseApi(retrofit: Retrofit) = retrofit.create(CourseApi::class.java)

    @Singleton
    @Provides
    fun provideCourseDao(appDatabase: AppDatabase) = appDatabase.courseDao()

    @Singleton
    @Provides
    fun provideCourseMapper() = CourseMapper()

    @Singleton
    @Provides
    fun provideCourseworkDeadlineMapper(courseMapper: CourseMapper) = CourseworkDeadlineMapper(courseMapper)
}