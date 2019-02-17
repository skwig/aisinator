package sk.skwig.aisinator.common.lesson.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sk.skwig.aisinator.common.auth.COOKIE_HEADER

interface LessonRetrofitApi {

    @GET("auth/katalog/rozvrhy_view.pl")
    fun getLessons(
        @Header(COOKIE_HEADER) sessionCookie: String,
        @Query("format") format: String = "html",
        @Query("rozvrh_student") studentId: Long = 79489, // TODO: fetch student id
        @Query("zobraz") show: Long = 1
    ): Single<ResponseBody>

}