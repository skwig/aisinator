package sk.skwig.aisinator.feature.deadline.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sk.skwig.aisinator.feature.auth.COOKIE_HEADER

interface DeadlineRetrofitApi {

    @GET("auth/student/odevzdavarny.pl")
    fun getDeadlines(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long,
        @Query("obdobi") term: Long
    ): Single<ResponseBody>

    @GET("auth/student/odevzdavarny.pl")
    fun getCurrentDeadlines(
        @Header(COOKIE_HEADER) sessionCookie: String
    ): Single<ResponseBody>
}