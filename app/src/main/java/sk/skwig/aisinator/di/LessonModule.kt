package sk.skwig.aisinator.di

import sk.skwig.aisinator.common.data.db.mapper.LessonMapper
import sk.skwig.aisinator.common.data.db.mapper.LessonTimeMapper
import sk.skwig.aisinator.common.data.db.mapper.UpcomingLessonWithCourseMapper
import sk.skwig.aisinator.common.lesson.LessonRepository
import sk.skwig.aisinator.common.lesson.LessonRepositoryImpl
import sk.skwig.aisinator.common.lesson.api.*
import sk.skwig.aisinator.common.lesson.db.LessonDao
import sk.skwig.aisinator.common.lesson.db.LessonDaoImpl
import sk.skwig.aisinator.common.lesson.db.LessonRoomDao
import javax.inject.Singleton

class LessonModule {

    @Singleton
    val lessonRepository: LessonRepository by lazy {
        LessonRepositoryImpl(
            authManager,
            lessonApi,
            lessonDao
        )
    }

    @Singleton
    val lessonHtmlParser: LessonHtmlParser by lazy {
        LessonHtmlParserImpl()
    }

    @Singleton
    val lessonRetrofitApi: LessonRetrofitApi by lazy {
        retrofit.create(
            LessonRetrofitApi::class.java
        )
    }

    @Singleton
    val deadlineApi: LessonApi by lazy {
        LessonApiImpl(lessonRetrofitApi, lessonHtmlParser)
    }

    @Singleton
    val lessonRoomDao: LessonRoomDao by lazy {
        lessonDaoProvider.lessonDao()
    }


    @Singleton
    val lessonDao: LessonDao by lazy {
        LessonDaoImpl(
            lessonRoomDao,
            courseMapper,
            lessonMapper,
            upcomingLessonWithCourseMapper
        )
    }


    @Singleton
    val lessonMapper by lazy {
        LessonMapper(lessonTimeMapper)
    }

    @Singleton
    val provideLessonTimeMapper by lazy { LessonTimeMapper() }

    @Singleton
    val upcomingLessonWithCourseMapper by lazy {
        UpcomingLessonWithCourseMapper(
            courseMapper,
            lessonMapper,
            lessonTimeMapper
        )
    }
}