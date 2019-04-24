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
    fun getLessons() : List<Lesson>
}

internal class LessonRepositoryImpl(
    private val cacheLessonStorage: CacheStorage<Lesson>,
    private val databaseLessonStorage: DatabaseStorage<Lesson>,
    private val networkLessonStorage: NetworkStorage<Lesson>
) : LessonRepository {

    override fun getLessons(): List<Lesson> {
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

        throw RuntimeException()
    }
}

