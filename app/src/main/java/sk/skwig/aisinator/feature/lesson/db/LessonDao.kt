package sk.skwig.aisinator.feature.lesson.db

import sk.skwig.aisinator.feature.course.db.CourseMapper
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.UpcomingLesson
import java.time.Instant

interface LessonDao {
    fun insertLessons(lessons: List<Lesson>)
    fun loadUpcomingLessons(instant: Instant): List<UpcomingLesson>
    fun loadLessons(): List<Lesson>
}

internal class LessonDaoImpl(
    private val dao: LessonRoomDao,
    private val lessonWithCourseMapper: LessonWithCourseMapper,
    private val upcomingLessonMapper: UpcomingLessonMapper,
    private val lessonMapper: LessonMapper,
    private val courseMapper: CourseMapper
) : LessonDao {

    override fun insertLessons(lessons: List<Lesson>) =
        dao.insertLessonsWithCourses(
            courseMapper.toEntityList(lessons.map { it.course }),
            lessonMapper.toEntityList(lessons)
        )

    override fun loadUpcomingLessons(instant: Instant) =
        dao.loadUpcomingLessons(instant).let { upcomingLessonMapper.fromEntityList(it) }

    override fun loadLessons() =
        dao.loadLessons().let { lessonWithCourseMapper.fromEntityList(it) }
}




