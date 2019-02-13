package sk.skwig.aisinator.dashboard.api

import io.reactivex.Single
import sk.skwig.aisinator.auth.Authentication
import sk.skwig.aisinator.common.util.toDocument
import sk.skwig.aisinator.dashboard.Course
import sk.skwig.aisinator.dashboard.Coursework
import sk.skwig.aisinator.dashboard.Deadline
import sk.skwig.aisinator.dashboard.Lesson

class DashboardApi(
    private val api: DashboardRetrofitApi,
    private val htmlParser: DashboardHtmlParser
) {

    fun getActiveCourses(authentication: Authentication): Single<List<Course>> =
        api.getActiveCourses(authentication.cookie)
            .map { htmlParser.parseActiveCourses(it.toDocument()) }

    fun getCoursework(authentication: Authentication, course: Course): Single<Coursework> =
        api.getCoursework(authentication.cookie, course = course.id)
            .map { htmlParser.parseCoursework(it.toDocument()) }

    fun getDeadlines(authentication: Authentication): Single<List<Deadline>> =
        api.getDeadlines(authentication.cookie)
            .map { htmlParser.parseDeadlines(it.toDocument()) }

    fun getLessons(authentication: Authentication): Single<List<Lesson>> =
        api.getLessons(authentication.cookie)
            .map { htmlParser.parseLessons(it.toDocument()) }

}