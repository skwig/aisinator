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
import sk.skwig.aisinator.common.auth.AuthMessageBus
import sk.skwig.aisinator.common.auth.AuthTimedOutException
import sk.skwig.aisinator.common.auth.di.AIS
import sk.skwig.aisinator.common.auth.di.AIS_AUTH
import sk.skwig.aisinator.common.auth.di.DASHBOARD
import java.net.CookieManager
import javax.inject.Named
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

    @Provides
    @Singleton
    @Named(AIS)
    fun provideAisRetrofit(@Named(AIS_AUTH) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://is.stuba.sk/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named(DASHBOARD)
    fun provideDashboardRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://aisdashboard.herokuapp.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named(AIS_AUTH)
    fun provideAisAuthOkHttpClient(
        sessionInterceptor: SessionInterceptor,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(sessionInterceptor.cookieJar)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(sessionInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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

    @Provides
    @Singleton
    fun provideAuthInterceptor(authMessageBus: AuthMessageBus): AuthInterceptor {
        return AuthInterceptor(authMessageBus)
    }
}