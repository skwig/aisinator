package sk.skwig.aisinator.feature.lesson.db

import androidx.room.*
import sk.skwig.aisinator.feature.lesson.LessonEntity
import sk.skwig.aisinator.feature.lesson.LessonWithCourseEntity
import sk.skwig.aisinator.feature.lesson.UpcomingLessonEntity
import java.time.Instant

@Dao
interface LessonRoomDao {

    @Insert
    fun insertLessons(deadlines: List<LessonEntity>)

    @Insert
    fun insertLessonsWithCourses(lessons: List<LessonWithCourseEntity>)

    @Query("""SELECT *,
            strftime('%s', datetime(date(:instant, 'unixepoch'), time(lesson_start_time), 'weekday ' || lesson_start_day_of_week )) AS upcoming_start,
            strftime('%s', datetime(date(:instant, 'unixepoch'), time(lesson_end_time), 'weekday ' || lesson_end_day_of_week )) AS upcoming_end
            FROM lessons l
            JOIN courses c ON c.course_id = l.fk_course_id
            WHERE upcoming_end > :instant
            AND lesson_end_day_of_week == strftime('%w', datetime(:instant, 'unixepoch'))""")
    fun loadUpcomingLessons(instant: Instant): List<UpcomingLessonEntity>

    @Query("""SELECT *
         FROM lessons l JOIN courses c ON c.course_id = l.fk_course_id""")
    fun loadLessons(): List<LessonWithCourseEntity>
}








