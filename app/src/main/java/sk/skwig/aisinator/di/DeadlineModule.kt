package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.data.db.mapper.DeadlineMapper
import sk.skwig.aisinator.common.data.db.mapper.DeadlineWithCourseMapper
import sk.skwig.aisinator.common.deadline.DeadlineRepository
import sk.skwig.aisinator.common.deadline.DeadlineRepositoryImpl
import sk.skwig.aisinator.common.deadline.api.*
import sk.skwig.aisinator.common.deadline.db.DeadlineDao
import sk.skwig.aisinator.common.deadline.db.DeadlineDaoImpl
import sk.skwig.aisinator.common.deadline.db.DeadlineRoomDao
import javax.inject.Singleton

class DeadlineModule {

    @Singleton
    val provideDeadlineRepository: DeadlineRepository by lazy {
        DeadlineRepositoryImpl(
            authManager,
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
        retrofit.create(DeadlineRetrofitApi::class.java)
    }

    @Singleton
    val deadlineApi: DeadlineApi by lazy {
        DeadlineApiImpl(deadlineRetrofitApi, deadlineHtmlParser)
    }

    @Singleton
    val deadlineRoomDao: DeadlineRoomDao by lazy {
        deadlineDaoProvider.deadlineDao()
    }

    @Singleton
    val deadlineDao: DeadlineDao by lazy {
        DeadlineDaoImpl(
            deadlineRoomDao,
            courseMapper,
            deadlineMapper,
            deadlineWithCourseMapper
        )
    }

    @Singleton
    val provideDeadlineMapper: DeadlineMapper by lazy {
        DeadlineMapper()
    }

    @Singleton
    val deadlineWithCourseMapper: DeadlineWithCourseMapper by lazy {
        DeadlineWithCourseMapper(courseMapper, deadlineMapper)
    }
}
