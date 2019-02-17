package sk.skwig.aisinator.dashboard

import android.util.Log
import io.reactivex.Observable
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.dashboard.api.LessonApi
import sk.skwig.aisinator.dashboard.db.LessonDao
import java.time.Instant

interface LessonRepository {
    fun getTodaysUpcomingLessons(): Observable<List<UpcomingLesson>>
}

internal class LessonRepositoryImpl(
    private val authManager: AuthManager,
    private val lessonApi: LessonApi,
    private val lessonDao: LessonDao
) : LessonRepository {

    override fun getTodaysUpcomingLessons(/*student*/) =
        authManager.authentication
            .doOnNext { Log.d("matej", "LessonRepositoryImpl.getTodaysUpcomingLessons") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { lessonApi.getLessons(it) }
                    .concatMapCompletable { lessonDao.insertLessons(it) }
                    .andThen(lessonDao.loadUpcomingLessons(Instant.ofEpochSecond(1550102400)))
            }
}