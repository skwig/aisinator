package sk.skwig.aisinator.common.lesson.api

import io.reactivex.Single
import sk.skwig.aisinator.common.auth.Authentication
import sk.skwig.aisinator.common.data.Lesson
import sk.skwig.aisinator.common.util.toDocument


interface LessonApi {
    fun getLessons(authentication: Authentication): Single<List<Lesson>>
}

internal class LessonApiImpl(
    private val api: LessonRetrofitApi,
    private val htmlParser: LessonHtmlParser
) : LessonApi {

    override fun getLessons(authentication: Authentication): Single<List<Lesson>> =
        api.getLessons(authentication.cookie)
            .map { htmlParser.parseLessons(it.toDocument()) }

}