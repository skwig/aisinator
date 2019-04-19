package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.course.CourseRepository
import sk.skwig.aisinator.common.course.CourseRepositoryImpl
import sk.skwig.aisinator.common.course.api.*
import sk.skwig.aisinator.common.course.db.CourseDao
import sk.skwig.aisinator.common.course.db.CourseDaoImpl
import sk.skwig.aisinator.common.data.db.mapper.CourseMapper
import javax.inject.Singleton

class CourseModule {

    lateinit var authModule: AuthModule
    lateinit var networkModule: NetworkModule

    @Singleton
    val courseRepository: CourseRepository by lazy {
        CourseRepositoryImpl(
            authModule.authManager,
            courseApi,
            courseDao
        )
    }

    @Singleton
    val courseHtmlParser: CourseHtmlParser by lazy {
        CourseHtmlParserImpl()
    }

    @Singleton
    val courseRetrofitApi: CourseRetrofitApi by lazy {
        networkModule.aisRetrofit.create(
            CourseRetrofitApi::class.java
        )
    }

    @Singleton
    val courseApi: CourseApi by lazy {
        CourseApiImpl(courseRetrofitApi, courseHtmlParser)
    }

    @Singleton
    val courseRoomDao by lazy { courseDaoProvider.courseDao() }

    @Singleton
    val courseDao: CourseDao by lazy {
        CourseDaoImpl(courseRoomDao, courseMapper)
    }

    @Singleton
    val courseMapper by lazy { CourseMapper() }

}
