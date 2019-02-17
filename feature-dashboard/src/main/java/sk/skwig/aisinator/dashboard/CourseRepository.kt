package sk.skwig.aisinator.dashboard

import android.util.Log
import io.reactivex.Observable
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.dashboard.api.CourseApi
import sk.skwig.aisinator.dashboard.db.CourseDao

interface CourseRepository {
    fun getActiveCourses(): Observable<List<Course>>
    fun getCoursework(course: Course): Observable<Coursework>
}

internal class CourseRepositoryImpl(
    private val authManager: AuthManager,
    private val courseApi: CourseApi,
    private val courseDao: CourseDao
) : CourseRepository {

    override fun getActiveCourses(): Observable<List<Course>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepositoryImpl.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getActiveCourses(it) }
                    .concatMapCompletable { courseDao.insertCourses(it) }
                    .andThen(courseDao.loadAllCourses())
            }

    override fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepositoryImpl.getCoursework") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getCoursework(it, course) }
            }
}