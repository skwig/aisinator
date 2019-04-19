package sk.skwig.aisinator.common.lesson

import io.reactivex.Observable
import sk.skwig.aisinator.common.data.Lesson
import sk.skwig.aisinator.common.data.UpcomingLesson

interface LessonRepository {
    fun getTodaysUpcomingLessons(): Observable<List<UpcomingLesson>>
}

interface LessonStorage {
    fun getLessons(): List<Lesson>?
}

interface LocalLessonStorage : LessonStorage {
    fun putLessons(lessons: List<Lesson>)
}

class CacheLessonStorage : LocalLessonStorage {

    private var cache: List<Lesson>? = null

    override fun getLessons(): List<Lesson>? {
        return cache
    }

    override fun putLessons(lessons: List<Lesson>) {
        cache = lessons
    }

    fun invalidateCache() {
        cache = null
    }
}

class DatabaseLessonStorage : LocalLessonStorage {
    override fun getLessons(): List<Lesson>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putLessons(lessons: List<Lesson>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun putLesson(lesson: Lesson) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun deleteLesson(lesson: Lesson) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class NetworkLessonStorage : LessonStorage {
    override fun getLessons(): List<Lesson>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

internal class LessonRepositoryImpl(
    private val cacheLessonHandler: CacheLessonStorage,
    private val databaseLessonHandler: DatabaseLessonStorage,
    private val networkLessonHandler: NetworkLessonStorage
) : LessonRepository {

    fun getLessons(): List<Lesson> {
        val cachedData = cacheLessonHandler.getLessons()
        if (cachedData != null) {
            return cachedData
        }

        val localDatabaseData = databaseLessonHandler.getLessons()
        if (localDatabaseData != null) {
            cacheLessonHandler.putLessons(localDatabaseData)
            return localDatabaseData
        }

        val networkData = networkLessonHandler.getLessons()
        if (networkData != null) {
            cacheLessonHandler.putLessons(networkData)
            databaseLessonHandler.putLessons(networkData)
            return networkData
        }

        throw RuntimeException("Cannot")
    }

    fun saveLesson(lesson: Lesson) {
        databaseLessonHandler.putLesson(lesson)
        cacheLessonHandler.invalidateCache()
    }

    fun deleteLesson(lesson: Lesson) {
        databaseLessonHandler.deleteLesson(lesson)
        cacheLessonHandler.invalidateCache()
    }
}