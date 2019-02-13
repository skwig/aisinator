package sk.skwig.aisinator.dashboard.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.dashboard.Lesson
import sk.skwig.aisinator.dashboard.UpcomingLesson
import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao

interface LessonDao {
    fun insertLessons(deadlines: List<Lesson>): Completable
    fun loadAllUpcomingLessons(): Observable<List<UpcomingLesson>>
}

class LessonDaoImpl(
    private val dao: LessonRoomDao,
    private val courseMapper: CourseMapper,
    private val lessonMapper: LessonMapper,
    private val upcomingLessonWithCourseMapper: UpcomingLessonWithCourseMapper
) : LessonDao {

    override fun insertLessons(lessons: List<Lesson>): Completable =
        Completable.fromAction {
            dao.insertLessonsWithCourses(
                courseMapper.toEntityList(lessons.map { it.course }),
                lessonMapper.toEntityList(lessons)
            )
        }

    override fun loadAllUpcomingLessons(): Observable<List<UpcomingLesson>> =
        dao.loadAllUpcomingLessons()
            .map { upcomingLessonWithCourseMapper.fromEntityList(it) }

}