package sk.skwig.aisinator.dashboard.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val COOKIE_HEADER = "Cookie"

interface DashboardRetrofitApi {

    @GET("auth/student/list.pl")
    fun getActiveCourses(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long = 151408,
        @Query("obdobi") term: Long = 526
    ): Single<ResponseBody>

    @GET("auth/student/list.pl")
    fun getCoursework(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long = 151408,
        @Query("obdobi") term: Long = 526,
        @Query("predmet") course: Long,
        @Query("zobraz_prubezne") show: Long = 1
    ): Single<ResponseBody>

    @GET("auth/student/odevzdavarny.pl")
    fun getDeadlines(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long = 151408,
        @Query("obdobi") term: Long = 526
    ): Single<ResponseBody>

    @GET("auth/katalog/rozvrhy_view.pl")
    fun getLessons(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("format") format: String = "html",
        @Query("rozvrh_student") studentId: Long = 79489, // TODO: fetch student id
        @Query("zobraz") show: Long = 1
    ): Single<ResponseBody>

}