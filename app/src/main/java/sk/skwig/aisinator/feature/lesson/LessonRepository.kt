package sk.skwig.aisinator.feature.lesson

import io.reactivex.Observable
import sk.skwig.aisinator.common.util.storage.CacheStorage
import sk.skwig.aisinator.common.util.storage.DatabaseStorage
import sk.skwig.aisinator.common.util.storage.NetworkStorage
import sk.skwig.aisinator.feature.auth.AuthManager

interface LessonRepository {
    fun getTodaysUpcomingLessons(): Observable<List<UpcomingLesson>>
}

internal class LessonRepositoryImpl(
    private val authManager: AuthManager,
    private val cacheLessonStorage: CacheStorage<Lesson>,
    private val databaseLessonStorage: DatabaseStorage<Lesson>,
    private val networkLessonStorage: NetworkStorage<Lesson>
) : LessonRepository {

    override fun getTodaysUpcomingLessons(): Observable<List<UpcomingLesson>> {
        return Observable.never()
    }

    fun getLessons(): List<Lesson> {
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