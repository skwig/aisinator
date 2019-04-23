package sk.skwig.aisinator.feature.deadline.api

import io.reactivex.Single
import sk.skwig.aisinator.feature.auth.Authentication
import sk.skwig.aisinator.feature.deadline.Deadline
import sk.skwig.aisinator.common.util.toDocument

interface DeadlineApi {
    fun getDeadlines(authentication: Authentication): Single<List<Deadline>>
}

internal class DeadlineApiImpl(
    private val api: DeadlineRetrofitApi,
    private val htmlParser: DeadlineHtmlParser
) : DeadlineApi {

    override fun getDeadlines(authentication: Authentication): Single<List<Deadline>> =
        api.getCurrentDeadlines(authentication.cookie)
            .map { htmlParser.parseDeadlines(it.toDocument()) }

}