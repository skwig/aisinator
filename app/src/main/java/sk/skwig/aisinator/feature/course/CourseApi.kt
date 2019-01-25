package sk.skwig.aisinator.feature.course

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val COOKIE_HEADER = "Cookie"

interface CourseApi {

    @GET("auth/student/moje_studium.pl")
    fun myStudy(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("_m") neviem: Long = 3110
    ): Single<Response<ResponseBody>>

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