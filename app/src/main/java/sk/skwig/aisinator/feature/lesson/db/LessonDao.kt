package sk.skwig.aisinator.feature.lesson.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.feature.course.db.CourseMapper
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.UpcomingLesson
import java.time.Instant

interface LessonDao {
    fun insertLessons(lessons: List<Lesson>): Completable
    fun loadUpcomingLessons(instant: Instant): Observable<List<UpcomingLesson>>
}

internal class LessonDaoImpl(
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