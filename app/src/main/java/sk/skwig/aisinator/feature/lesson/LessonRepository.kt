package sk.skwig.aisinator.feature.lesson

import android.util.Log
import io.reactivex.Observable
import sk.skwig.aisinator.common.util.storage.CacheStorage
import sk.skwig.aisinator.common.util.storage.DatabaseStorage
import sk.skwig.aisinator.common.util.storage.NetworkStorage
import sk.skwig.aisinator.feature.auth.AuthManager
import sk.skwig.aisinator.feature.lesson.api.LessonApi
import sk.skwig.aisinator.feature.lesson.db.LessonDao

interface LessonRepository {
    fun getUpcomingLessons(): Observable<List<UpcomingLesson>>
    fun getLessons() : Observable<List<Lesson>>
}

internal class LessonRepositoryImpl(
    private val authManager: AuthManager,
    private val lessonDao : LessonDao,
    private val lessonApi: LessonApi,
    private val cacheLessonStorage: CacheStorage<Lesson>,
    private val databaseLessonStorage: DatabaseStorage<Lesson>,
    private val networkLessonStorage: NetworkStorage<Lesson>
) : LessonRepository {

    override fun getUpcomingLessons(): Observable<List<UpcomingLesson>> {
        return Observable.never()
    }

    override fun getLessons(/*student*/) =
        authManager.authentication
            .doOnNext { Log.d("matej", "LessonRepository.getLessons") }
            .switchMap {
                Observable.just(it)
                    .flatMapSingle { lessonApi.getLessons(it) }
                    .concatMapCompletable { lessonDao.insertLessons(it) }
                    .andThen(lessonDao.loadLessons())
            }

    fun fooGetLessons(): List<Lesson> {
        val cachedData = cacheLessonStorage.getItems()
        if (cachedData != null) {
            return cachedData
        }

        val localDatabaseData = databaseLessonStorage.getItems()
        if (localDatabaseData != null) {
            cacheLessonStorage.putItems(localDatabaseData)
            return localDatabaseData
        }

        val networkData = networkLessonStorage.getItems()
        if (networkData != null) {
            cacheLessonStorage.putItems(networkData)
            databaseLessonStorage.putItems(networkData)
            return networkData
        }

        throw RuntimeException("Cannot")
    }

    fun saveLesson(lesson: Lesson) {
        databaseLessonStorage.putItem(lesson)
        cacheLessonStorage.invalidateCache()
    }

    fun deleteLesson(lesson: Lesson) {
        databaseLessonStorage.deleteItem(lesson)
        cacheLessonStorage.invalidateCache()
    }
}