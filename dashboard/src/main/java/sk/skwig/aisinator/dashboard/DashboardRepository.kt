package sk.skwig.aisinator.dashboard

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.auth.AuthManager
import sk.skwig.aisinator.dashboard.api.DashboardApi
import sk.skwig.aisinator.dashboard.db.CourseDao
import sk.skwig.aisinator.dashboard.db.DeadlineDao
import sk.skwig.aisinator.dashboard.db.LessonDao
import java.time.Instant

interface DashboardRepository {
    fun getActiveCourses(): Observable<List<Course>>
    fun getCoursework(course: Course): Observable<Coursework>
    fun getActiveCourseworkDeadlines(): Observable<List<Deadline>>

    fun dismissCourseworkDeadline(deadline: Deadline): Completable
    fun getTodaysUpcomingLessons(): Observable<List<UpcomingLesson>>
}

class DashboardRepositoryImpl(
    private val authManager: AuthManager,
    private val dashboardApi: DashboardApi,
    private val courseDao: CourseDao,
    private val deadlineDao: DeadlineDao,
    private val lessonDao: LessonDao
) : DashboardRepository {

    override fun getActiveCourses(): Observable<List<Course>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { dashboardApi.getActiveCourses(it) }
                    .concatMapCompletable { courseDao.insertCourses(it) }
                    .andThen(courseDao.loadAllCourses())
            }

    override fun getActiveCourseworkDeadlines(): Observable<List<Deadline>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { dashboardApi.getDeadlines(it) }
                    .concatMapCompletable { deadlineDao.insertCourseworkDeadlines(it) }
                    .andThen(deadlineDao.loadAllCourses())
            }

    override fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getCoursework") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { dashboardApi.getCoursework(it, course) }
            }

    override fun getTodaysUpcomingLessons(/*student*/) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getTodaysUpcomingLessons") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { dashboardApi.getLessons(it) }
                    .concatMapCompletable { lessonDao.insertLessons(it) }
                    .andThen(lessonDao.loadUpcomingLessons(Instant.ofEpochSecond(1550102400)))
            }

    override fun dismissCourseworkDeadline(deadline: Deadline): Completable =
        deadlineDao.updateCourseworkDeadline(
            deadline.copy(isDismissed = true)
        )
}