package sk.skwig.aisinator.common.deadline.api

import io.reactivex.Single
import sk.skwig.aisinator.common.auth.Authentication
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.util.toDocument

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