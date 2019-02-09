package sk.skwig.aisinator.feature.course

import android.util.Log
import io.reactivex.Observable
import sk.skwig.aisinator.common.util.toDocument
import sk.skwig.aisinator.feature.auth.AuthManager

interface CourseRepository {
    fun getActiveCourses(): Observable<List<Course>>
    fun getCoursework(course: Course): Observable<Coursework>
    fun getActiveCourseworkDeadlines(): Observable<List<CourseworkDeadline>>
}

class CourseRepositoryImpl(
    private val authManager: AuthManager,
    private val courseApi: CourseApi,
    private val htmlParser: CourseHtmlParser,
    private val courseDao: CourseDao,
    private val courseMapper: CourseMapper,
    private val courseworkDeadlineMapper: CourseworkDeadlineMapper
) : CourseRepository {


    init {

    }

    override fun getActiveCourses(): Observable<List<Course>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getActiveCourses(it.cookie) }
                    .map { htmlParser.parseActiveCourses(it.toDocument()) }
                    .map { courseMapper.toEntityList(it) }
                    .concatMapCompletable { courseDao.insertCourses(it) }
                    .andThen(courseDao.loadAllCourses())
                    .map { courseMapper.fromEntityList(it) }
            }

    override fun getActiveCourseworkDeadlines() : Observable<List<CourseworkDeadline>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { courseApi.getCourseworkDeadlines(it.cookie) }
                    .map { htmlParser.parseCourseworkDeadlines(it.toDocument()) }
                    .map { courseworkDeadlineMapper.toRelationList(it) }
                    .concatMapCompletable { courseDao.insertCoursesWithDeadlines(it) }
                    .andThen(courseDao.loadAllCoursesWithDeadlines())
                    .map {courseworkDeadlineMapper.fromRelationList(it)}
            }

    override fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej", "CourseRepository.getCoursework") }
            .flatMapSingle { courseApi.getCoursework(it.cookie, course = course.id) }
            .map { htmlParser.parseCoursework(it.toDocument()) }
}