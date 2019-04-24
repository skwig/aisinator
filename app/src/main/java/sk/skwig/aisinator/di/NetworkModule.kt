package sk.skwig.aisinator.di

import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import sk.skwig.aisinator.feature.auth.AuthMessageBus
import sk.skwig.aisinator.feature.auth.AuthTimedOutException
import sk.skwig.aisinator.feature.auth.di.AIS
import sk.skwig.aisinator.feature.auth.di.AIS_AUTH
import sk.skwig.aisinator.feature.auth.di.AuthModule
import sk.skwig.aisinator.feature.auth.di.DASHBOARD
import java.net.CookieManager
import javax.inject.Named
import javax.inject.Singleton

private const val SESSION_COOKIE_NAME = "UISAuth"

class NetworkModule {

    lateinit var authModule: AuthModule

    class SessionInterceptor : Interceptor {

        private val cookieManager = CookieManager()
        val cookieJar = JavaNetCookieJar(cookieManager)

        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val sessionCookie = cookieManager.cookieStore.cookies.firstOrNull { it.name == SESSION_COOKIE_NAME }

            cookieManager.cookieStore.removeAll()

            if (sessionCookie == null) {
                return response
            }

            return response.newBuilder()
                .addHeader(sessionCookie.name, sessionCookie.value)
                .build()
        }
    }

    class AuthInterceptor(private val authMessageBus: AuthMessageBus) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (response.code() == 403) {
                authMessageBus.onAuthTimedOut()
                throw AuthTimedOutException()
            }
            return response
        }
    }

    @Singleton
    @Named(AIS)
    val aisRetrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://is.stuba.sk/")
            .client(aisAuthOkHttpClient)
            .build()
    }

    @Singleton
    @Named(AIS_AUTH)
    val aisAuthOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(sessionInterceptor.cookieJar)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(sessionInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Named(DASHBOARD)
    val dashboardRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ooans.000.webhostapp.com/prod/")
            .client(okHttpClient)
            .build()
    }

    @Singleton
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Singleton
    val sessionInterceptor: SessionInterceptor by lazy {
        SessionInterceptor()
    }

    @Singleton
    val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(authModule.authMessageBus)
    }
}