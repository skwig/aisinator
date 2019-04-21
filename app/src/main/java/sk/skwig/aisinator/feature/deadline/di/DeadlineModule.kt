package sk.skwig.aisinator.feature.deadline.di

import sk.skwig.aisinator.di.NetworkModule
import sk.skwig.aisinator.di.PersistenceModule
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.course.di.CourseModule
import sk.skwig.aisinator.feature.deadline.db.DeadlineMapper
import sk.skwig.aisinator.feature.deadline.db.DeadlineWithCourseMapper
import sk.skwig.aisinator.feature.deadline.DeadlineRepository
import sk.skwig.aisinator.feature.deadline.DeadlineRepositoryImpl
import sk.skwig.aisinator.feature.deadline.api.*
import sk.skwig.aisinator.feature.deadline.db.DeadlineDao
import sk.skwig.aisinator.feature.deadline.db.DeadlineDaoImpl
import sk.skwig.aisinator.feature.deadline.db.DeadlineRoomDao
import javax.inject.Singleton

class DeadlineModule {

    lateinit var authModule: AuthModule
    lateinit var networkModule: NetworkModule
    lateinit var courseModule: CourseModule
    lateinit var persistenceModule: PersistenceModule

    @Singleton
    val deadlineRepository: DeadlineRepository by lazy {
        DeadlineRepositoryImpl(
            authModule.authManager,
            deadlineApi,
            deadlineDao
        )
    }

    @Singleton
    val deadlineHtmlParser: DeadlineHtmlParser by lazy {
        DeadlineHtmlParserImpl()
    }

    @Singleton
    val deadlineRetrofitApi by lazy {
        networkModule.aisRetrofit.create(DeadlineRetrofitApi::class.java)
    }

    @Singleton
    val deadlineApi: DeadlineApi by lazy {
        DeadlineApiImpl(deadlineRetrofitApi, deadlineHtmlParser)
    }

    @Singleton
    val deadlineRoomDao: DeadlineRoomDao by lazy {
        persistenceModule.appDatabase.deadlineDao()
    }

    @Singleton
    val deadlineDao: DeadlineDao by lazy {
        DeadlineDaoImpl(
            deadlineRoomDao,
            courseModule.courseMapper,
            deadlineMapper,
            deadlineWithCourseMapper
        )
    }

    @Singleton
    val deadlineMapper: DeadlineMapper by lazy {
        DeadlineMapper()
    }

    @Singleton
    val deadlineWithCourseMapper: DeadlineWithCourseMapper by lazy {
        DeadlineWithCourseMapper(courseModule.courseMapper, deadlineMapper)
    }
}
