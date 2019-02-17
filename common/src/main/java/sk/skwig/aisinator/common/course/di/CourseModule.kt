package sk.skwig.aisinator.common.course.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.course.*
import sk.skwig.aisinator.common.course.api.*
import sk.skwig.aisinator.common.course.db.CourseDao
import sk.skwig.aisinator.common.course.db.CourseDaoImpl
import sk.skwig.aisinator.common.course.db.CourseDaoProvider
import sk.skwig.aisinator.common.course.db.CourseRoomDao
import sk.skwig.aisinator.common.course.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.common.data.db.mapper.CourseMapper
import sk.skwig.aisinator.common.di.util.ViewModelKey
import javax.inject.Singleton

@Module(includes = [CourseViewModelModule::class])
class CourseModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        authManager: AuthManager,
        courseApi: CourseApi,
        courseDao: CourseDao
    ): CourseRepository =
        CourseRepositoryImpl(
            authManager,
            courseApi,
            courseDao
        )

    @Provides
    @Singleton
    fun provideCourseHtmlParser(): CourseHtmlParser =
        CourseHtmlParserImpl()

    @Singleton
    @Provides
    fun provideCourseRetrofitApi(retrofit: Retrofit): CourseRetrofitApi = retrofit.create(
        CourseRetrofitApi::class.java)

    @Singleton
    @Provides
    fun provideCourseApi(courseRetrofitApi: CourseRetrofitApi, courseHtmlParser: CourseHtmlParser): CourseApi =
        CourseApiImpl(courseRetrofitApi, courseHtmlParser)

    @Singleton
    @Provides
    fun provideCourseRoomDao(courseDaoProvider: CourseDaoProvider) = courseDaoProvider.courseDao()

    @Singleton
    @Provides
    fun provideCourseDao(courseRoomDao: CourseRoomDao, courseMapper: CourseMapper): CourseDao =
        CourseDaoImpl(courseRoomDao, courseMapper)

    @Singleton
    @Provides
    fun provideCourseMapper() = CourseMapper()

}

@Module
abstract class CourseViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActiveCoursesViewModel::class)
    abstract fun bindActiveCoursesViewModel(activeCoursesViewModel: ActiveCoursesViewModel): ViewModel
}