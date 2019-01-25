package sk.skwig.aisinator.feature.course.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sk.skwig.aisinator.feature.auth.AuthApi
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.course.CourseApi
import sk.skwig.aisinator.feature.course.CourseHtmlParser
import sk.skwig.aisinator.feature.course.CourseRepository
import javax.inject.Singleton

@Module
class CourseModule {

    @Provides
    @Singleton
    fun provideCourseManager(authManager: AuthManager, courseApi: CourseApi, courseHtmlParser: CourseHtmlParser) = CourseRepository(authManager, courseApi, courseHtmlParser)

    @Provides
    @Singleton
    fun provideCourseHtmlParser() = CourseHtmlParser()

    @Singleton
    @Provides
    fun provideCourseApi(retrofit: Retrofit) = retrofit.create(CourseApi::class.java)
}