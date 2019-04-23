package sk.skwig.aisinator.feature.course.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sk.skwig.aisinator.feature.auth.COOKIE_HEADER

interface CourseRetrofitApi {

    @GET("auth/student/list.pl")
    fun getCourses(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long,
        @Query("obdobi") term: Long
    ): Single<ResponseBody>

    @GET("auth/student/list.pl")
    fun getCoursework(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("studium") study: Long,
        @Query("obdobi") term: Long,
        @Query("predmet") course: Long,
        @Query("zobraz_prubezne") show: Long = 1
    ): Single<ResponseBody>

    @GET("auth/student/list.pl")
    fun getCurrentCourses(
        @Header(COOKIE_HEADER) sessionCookie: String
    ): Single<ResponseBody>

    @GET("auth/student/list.pl")
    fun getCurrentCoursework(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("predmet") course: Long,
        @Query("zobraz_prubezne") show: Long = 1
    ): Single<ResponseBody>

}