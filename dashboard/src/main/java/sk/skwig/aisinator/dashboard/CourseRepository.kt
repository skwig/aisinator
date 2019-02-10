package sk.skwig.aisinator.dashboard

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.auth.AuthManager
import sk.skwig.aisinator.common.util.toDocument
import sk.skwig.aisinator.dashboard.api.CourseApi
import sk.skwig.aisinator.dashboard.api.CourseHtmlParser
import sk.skwig.aisinator.dashboard.db.CourseDao
import sk.skwig.aisinator.dashboard.db.DeadlineDao

interface CourseRepository {
    fun getActiveCourses(): Observable<List<Course>>
    fun getCoursework(course: Course): Observable<Coursework>
    fun getActiveCourseworkDeadlines(): Observable<List<Deadline>>

    fun dismissCourseworkDeadline(deadline: Deadline): Completable
}

class CourseRepositoryImpl(
    private val authManager: AuthManager,
    private val courseApi: CourseApi,
    private val htmlParser: CourseHtmlParser,
    private val courseDao: CourseDao,
    private val deadlineDao: DeadlineDao
) : CourseRepository {

    override fun getActiveCourses(): Observable<List<Course>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getActiveCourses(it.cookie) }
                    .map { htmlParser.parseActiveCourses(it.toDocument()) }
                    .concatMapCompletable { courseDao.insertCourses(it) }
                    .andThen(courseDao.loadAllCourses())
            }

    override fun getActiveCourseworkDeadlines(): Observable<List<Deadline>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getCourseworkDeadlines(it.cookie) }
                    .map { htmlParser.parseCourseworkDeadlines(it.toDocument()) }
                    .concatMapCompletable { deadlineDao.insertCourseworkDeadlines(it) }
                    .andThen(deadlineDao.loadAllCourses())
            }

    override fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getCoursework") }
            .flatMapSingle { courseApi.getCoursework(it.cookie, course = course.id) }
            .map { htmlParser.parseCoursework(it.toDocument()) }

    override fun dismissCourseworkDeadline(deadline: Deadline): Completable =
        deadlineDao.updateCourseworkDeadline(
            deadline.copy(isDismissed = true)
        )
}