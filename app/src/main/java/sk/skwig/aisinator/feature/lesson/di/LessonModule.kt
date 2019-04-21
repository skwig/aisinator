package sk.skwig.aisinator.feature.lesson.di

import sk.skwig.aisinator.common.util.storage.CacheStorage
import sk.skwig.aisinator.common.util.storage.DatabaseStorage
import sk.skwig.aisinator.common.util.storage.NetworkStorage
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.course.di.CourseModule
import sk.skwig.aisinator.di.NetworkModule
import sk.skwig.aisinator.di.PersistenceModule
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.db.LessonMapper
import sk.skwig.aisinator.feature.lesson.db.LessonTimeMapper
import sk.skwig.aisinator.feature.lesson.db.UpcomingLessonWithCourseMapper
import sk.skwig.aisinator.feature.lesson.LessonRepository
import sk.skwig.aisinator.feature.lesson.LessonRepositoryImpl
import sk.skwig.aisinator.feature.lesson.api.*
import sk.skwig.aisinator.feature.lesson.db.LessonDao
import sk.skwig.aisinator.feature.lesson.db.LessonDaoImpl
import sk.skwig.aisinator.feature.lesson.db.LessonRoomDao
import javax.inject.Singleton

class LessonModule {

    lateinit var authModule: AuthModule
    lateinit var networkModule: NetworkModule
    lateinit var courseModule: CourseModule
    lateinit var persistenceModule: PersistenceModule

    @Singleton
    val lessonRepository: LessonRepository by lazy {
        LessonRepositoryImpl(
            authModule.authManager,
            lessonCacheStorage,
            lessonDatabaseStorage,
            lessonNetworkStorage
        )
    }

    @Singleton
    val lessonCacheStorage: CacheStorage<Lesson> by lazy {
        CacheStorage<Lesson>()
    }

    @Singleton
    val lessonDatabaseStorage: DatabaseStorage<Lesson> by lazy {
        DatabaseStorage<Lesson>()
    }

    @Singleton
    val lessonNetworkStorage: NetworkStorage<Lesson> by lazy {
        NetworkStorage<Lesson>()
    }

    @Singleton
    val lessonHtmlParser: LessonHtmlParser by lazy {
        LessonHtmlParserImpl()
    }

    @Singleton
    val lessonRetrofitApi: LessonRetrofitApi by lazy {
        networkModule.aisRetrofit.create(
            LessonRetrofitApi::class.java
        )
    }

    @Singleton
    val lessonApi: LessonApi by lazy {
        LessonApiImpl(lessonRetrofitApi, lessonHtmlParser)
    }

    @Singleton
    val lessonRoomDao: LessonRoomDao by lazy {
        persistenceModule.appDatabase.lessonDao()
    }


    @Singleton
    val lessonDao: LessonDao by lazy {
        LessonDaoImpl(
            lessonRoomDao,
            courseModule.courseMapper,
            lessonMapper,
            upcomingLessonWithCourseMapper
        )
    }


    @Singleton
    val lessonMapper by lazy {
        LessonMapper(lessonTimeMapper)
    }

    @Singleton
    val lessonTimeMapper by lazy { LessonTimeMapper() }

    @Singleton
    val upcomingLessonWithCourseMapper by lazy {
        UpcomingLessonWithCourseMapper(
            courseModule.courseMapper,
            lessonTimeMapper
        )
    }
}