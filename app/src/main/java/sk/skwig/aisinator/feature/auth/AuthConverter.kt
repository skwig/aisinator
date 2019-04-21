package sk.skwig.aisinator.feature.auth

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