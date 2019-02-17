package sk.skwig.aisinator.common.lesson.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.common.data.db.mapper.CourseMapper
import sk.skwig.aisinator.common.data.Lesson
import sk.skwig.aisinator.common.data.db.mapper.LessonMapper
import sk.skwig.aisinator.common.data.UpcomingLesson
import sk.skwig.aisinator.common.data.db.mapper.UpcomingLessonWithCourseMapper
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