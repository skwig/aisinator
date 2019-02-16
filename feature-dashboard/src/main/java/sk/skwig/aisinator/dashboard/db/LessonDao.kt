package sk.skwig.aisinator.dashboard.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.dashboard.Lesson
import sk.skwig.aisinator.dashboard.UpcomingLesson
import sk.skwig.aisinator.dashboard.db.roomdao.LessonRoomDao
import java.time.Instant

interface LessonDao {
    fun insertLessons(lessons: List<Lesson>): Completable
    fun loadUpcomingLessons(instant: Instant): Observable<List<UpcomingLesson>>
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

    override fun loadUpcomingLessons(instant: Instant): Observable<List<UpcomingLesson>> =
        dao.loadUpcomingLessons(instant)
            .map { upcomingLessonWithCourseMapper.fromEntityList(it) }

}