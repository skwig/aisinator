package sk.skwig.aisinator.dashboard.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.di.util.ViewModelKey
import sk.skwig.aisinator.dashboard.DeadlineDaoProvider
import sk.skwig.aisinator.dashboard.DeadlineRepository
import sk.skwig.aisinator.dashboard.DeadlineRepositoryImpl
import sk.skwig.aisinator.dashboard.api.*
import sk.skwig.aisinator.dashboard.db.*
import sk.skwig.aisinator.dashboard.db.roomdao.DeadlineRoomDao
import sk.skwig.aisinator.dashboard.viewmodel.DeadlinesViewModel
import javax.inject.Singleton

@Module(includes = [DeadlineViewModelModule::class])
class DeadlineModule {

    @Provides
    @Singleton
    fun provideDeadlineRepository(
        authManager: AuthManager,
        deadlineApi: DeadlineApi,
        deadlineDao: DeadlineDao
    ): DeadlineRepository =
        DeadlineRepositoryImpl(
            authManager,
            deadlineApi,
            deadlineDao
        )

    @Provides
    @Singleton
    fun provideDeadlineHtmlParser(): DeadlineHtmlParser = DeadlineHtmlParserImpl()

    @Singleton
    @Provides
    fun provideDeadlineRetrofitApi(retrofit: Retrofit) = retrofit.create(DeadlineRetrofitApi::class.java)

    @Singleton
    @Provides
    fun provideDeadlineApi(deadlineRetrofitApi: DeadlineRetrofitApi, deadlineHtmlParser: DeadlineHtmlParser): DeadlineApi =
        DeadlineApiImpl(deadlineRetrofitApi, deadlineHtmlParser)

    @Singleton
    @Provides
    fun provideDeadlineRoomDao(deadlineDaoProvider: DeadlineDaoProvider): DeadlineRoomDao =
        deadlineDaoProvider.deadlineDao()


    @Singleton
    @Provides
    fun provideDeadlineDao(
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
    fun provideDeadlineMapper(): DeadlineMapper = DeadlineMapper()

    @Singleton
    @Provides
    fun provideDeadlineWithCourseMapper(
        courseMapper: CourseMapper,
        deadlineMapper: DeadlineMapper
    ): DeadlineWithCourseMapper =
        DeadlineWithCourseMapper(courseMapper, deadlineMapper)
}

@Module
abstract class DeadlineViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DeadlinesViewModel::class)
    abstract fun bindDeadlinesViewModel(deadlinesViewModel: DeadlinesViewModel): ViewModel
}