package sk.skwig.aisinator.feature.course

import android.util.Log
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.util.toDocument
import timber.log.Timber

class CourseRepository(
    private val authManager: AuthManager,
    private val courseApi: CourseApi,
    private val htmlParser: CourseHtmlParser
) {

    fun getActiveCourses() =
        authManager.authentication
            .doOnNext { Log.d("matej","CourseRepository.getActiveCourses") }
            .flatMapSingle { courseApi.getActiveCourses(it.cookie) }
            .map { htmlParser.parseActiveCourses(it.toDocument()) }

    fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej","CourseRepository.getCoursework") }
            .flatMapSingle { courseApi.getCoursework(it.cookie, course = course.id) }
            .map { htmlParser.parseCoursework(it.toDocument()) }

}