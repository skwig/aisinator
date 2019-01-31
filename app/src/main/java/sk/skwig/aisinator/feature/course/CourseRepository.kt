package sk.skwig.aisinator.feature.course

import android.util.Log
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.util.replayLast
import sk.skwig.aisinator.util.toDocument

class CourseRepository(
    private val authManager: AuthManager,
    private val courseApi: CourseApi,
    private val htmlParser: CourseHtmlParser
) {

    // TODO: sql cachovanie
    fun getActiveCourses() =
        authManager.authentication
            .doOnNext { Log.d("matej","CourseRepository.getActiveCourses") }
            .flatMapSingle { courseApi.getActiveCourses(it.cookie) }
            .map { htmlParser.parseActiveCourses(it.toDocument()) }
    // replay last z nejakeho dovodu zabrani pustenie requestu onSubscribe. treba refcount alebo nieco? ako inak cachovat & sharovat ak to neni refcountom?
//            .replayLast()

    fun getCoursework(course: Course) =
        authManager.authentication
            .doOnNext { Log.d("matej","CourseRepository.getCoursework") }
            .flatMapSingle { courseApi.getCoursework(it.cookie, course = course.id) }
            .map { htmlParser.parseCoursework(it.toDocument()) }

}