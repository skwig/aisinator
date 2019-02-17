package sk.skwig.aisinator.dashboard.api

import io.reactivex.Single
import sk.skwig.aisinator.common.auth.Authentication
import sk.skwig.aisinator.common.util.toDocument
import sk.skwig.aisinator.dashboard.Course
import sk.skwig.aisinator.dashboard.Coursework


interface CourseApi {
    fun getActiveCourses(authentication: Authentication): Single<List<Course>>
    fun getCoursework(authentication: Authentication, course: Course): Single<Coursework>
}

internal class CourseApiImpl(
    private val api: CourseRetrofitApi,
    private val htmlParser: CourseHtmlParser
) : CourseApi {

    override fun getActiveCourses(authentication: Authentication): Single<List<Course>> =
        api.getActiveCourses(authentication.cookie)
            .map { htmlParser.parseActiveCourses(it.toDocument()) }

    override fun getCoursework(authentication: Authentication, course: Course): Single<Coursework> =
        api.getCoursework(authentication.cookie, course = course.id)
            .map { htmlParser.parseCoursework(it.toDocument()) }

}