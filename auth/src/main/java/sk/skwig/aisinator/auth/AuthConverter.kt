package sk.skwig.aisinator.auth

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type

//class AuthConverterFactory : Converter.Factory(){
//    override fun responseBodyConverter(
//        type: Type,
//        annotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): Converter<ResponseBody, *>? {
//        return AuthConverter()
//    }
//}
//
//class AuthConverter : Converter<Response<ResponseBody>, Authentication>{
//    override fun convert(value: Response<ResponseBody>): Authentication {
//        val session = value.headers()["UISAuth"]!!
//        value.body()
//        return Authentication("UISAuth", session)
//    }
//}