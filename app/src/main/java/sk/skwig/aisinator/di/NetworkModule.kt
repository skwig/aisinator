package sk.skwig.aisinator.di

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.CookieManager
import javax.inject.Singleton

private const val SESSION_COOKIE_NAME = "UISAuth"

@Module
class NetworkModule {

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

            return response.newBuilder().addHeader(sessionCookie.name, sessionCookie.value)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://is.stuba.sk/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionInterceptor: SessionInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(sessionInterceptor.cookieJar)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(sessionInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    fun provideSessionInterceptor(): SessionInterceptor {
        return SessionInterceptor()
    }
}