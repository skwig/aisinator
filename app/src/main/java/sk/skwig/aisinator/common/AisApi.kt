package sk.skwig.aisinator.common

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface AisApi {

    @FormUrlEncoded
    @POST("system/login.pl")
    fun login(
        @Field("credential_0") login: String,
        @Field("credential_1") password: String,
        @Field("credential_k") foo4 : String = "",
        @Field("credential_2") sessionDuration: String = "345600",
        @Field("login_hidden") foo1 : String = "1",
        @Field("destination") foo2 : String = "/auth/",
        @Field("auth_id_hidden") foo3 : String = "0"
    ): Single<ResponseBody>
}