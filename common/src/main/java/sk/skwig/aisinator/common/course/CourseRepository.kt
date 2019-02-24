package sk.skwig.aisinator.common.course

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.course.api.CourseApi
import sk.skwig.aisinator.common.course.db.CourseDao
import sk.skwig.aisinator.common.data.Course
import sk.skwig.aisinator.common.data.Coursework

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
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle { courseApi.getActiveCourses(it) }
                    .concatMapCompletable { courseDao.insertCourses(it) }
                    .andThen(courseDao.loadAllCourses())
            }

    override fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepositoryImpl.getCoursework") }
            .switchMap {
                Observable.just(it)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle { courseApi.getCoursework(it, course) }
            }
}