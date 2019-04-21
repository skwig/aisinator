package sk.skwig.aisinator.feature.course.di

import sk.skwig.aisinator.di.NetworkModule
import sk.skwig.aisinator.di.PersistenceModule
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.course.CourseRepository
import sk.skwig.aisinator.feature.course.CourseRepositoryImpl
import sk.skwig.aisinator.feature.course.api.*
import sk.skwig.aisinator.feature.course.db.CourseDao
import sk.skwig.aisinator.feature.course.db.CourseDaoImpl
import sk.skwig.aisinator.feature.course.db.CourseMapper
import javax.inject.Singleton

class CourseModule {

    lateinit var authModule: AuthModule
    lateinit var networkModule: NetworkModule
    lateinit var persistenceModule: PersistenceModule

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
    val courseRoomDao by lazy { persistenceModule.appDatabase.courseDao() }

    @Singleton
    val courseDao: CourseDao by lazy {
        CourseDaoImpl(courseRoomDao, courseMapper)
    }

    @Singleton
    val courseMapper by lazy { CourseMapper() }

}
