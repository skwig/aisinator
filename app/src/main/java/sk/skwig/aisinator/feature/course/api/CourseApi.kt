package sk.skwig.aisinator.feature.course.api

import io.reactivex.Single
import sk.skwig.aisinator.feature.auth.Authentication
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.course.Coursework
import sk.skwig.aisinator.common.util.toDocument


interface CourseApi {
    fun getActiveCourses(authentication: Authentication): Single<List<Course>>
    fun getCoursework(authentication: Authentication, course: Course): Single<Coursework>
}

internal class CourseApiImpl(
    private val api: CourseRetrofitApi,
    private val htmlParser: CourseHtmlParser
) : CourseApi {

    override fun getActiveCourses(authentication: Authentication): Single<List<Course>> =
        api.getCurrentCourses(authentication.cookie)
            .map { htmlParser.parseActiveCourses(it.toDocument()) }

    override fun getCoursework(authentication: Authentication, course: Course): Single<Coursework> =
        api.getCurrentCoursework(authentication.cookie, course = course.id)
            .map { htmlParser.parseCoursework(it.toDocument()) }

}