package sk.skwig.aisinator.dashboard.api

import io.reactivex.Single
import sk.skwig.aisinator.common.auth.Authentication
import sk.skwig.aisinator.common.util.toDocument
import sk.skwig.aisinator.dashboard.Deadline


interface DeadlineApi {
    fun getDeadlines(authentication: Authentication): Single<List<Deadline>>
}

internal class DeadlineApiImpl(
    private val api: DeadlineRetrofitApi,
    private val htmlParser: DeadlineHtmlParser
) : DeadlineApi {

    override fun getDeadlines(authentication: Authentication): Single<List<Deadline>> =
        api.getDeadlines(authentication.cookie)
            .map { htmlParser.parseDeadlines(it.toDocument()) }

}