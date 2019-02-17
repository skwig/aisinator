package sk.skwig.aisinator.common.course.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sk.skwig.aisinator.common.auth.COOKIE_HEADER

interface CourseRetrofitApi {

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

}