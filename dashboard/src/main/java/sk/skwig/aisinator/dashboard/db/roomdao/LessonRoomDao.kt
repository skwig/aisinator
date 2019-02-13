package sk.skwig.aisinator.dashboard.db.roomdao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.dashboard.Lesson
import sk.skwig.aisinator.dashboard.db.entity.CourseEntity
import sk.skwig.aisinator.dashboard.db.entity.DeadlineEntity
import sk.skwig.aisinator.dashboard.db.entity.LessonEntity
import sk.skwig.aisinator.dashboard.db.entity.UpcomingLessonWithCourse

@Dao
interface LessonRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLessons(deadlines: List<LessonEntity>): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLessonsWithCourses(
        courses: List<CourseEntity>,
        lessons: List<LessonEntity>
    ): Unit // TODO: zmenit na Completable ked to fixnu v Room libke

    @Transaction
    @Query(
        """SELECT *,
            1000 * strftime('%s', datetime(date('now'), time(lesson_start_time), 'weekday ' || lesson_start_day_of_week )) AS upcoming_start,
            1000 * strftime('%s', datetime(date('now'), time(lesson_end_time), 'weekday ' || lesson_end_day_of_week )) AS upcoming_end
            FROM lessons l
            JOIN courses c ON c.course_id = l.fk_course_id
            WHERE upcoming_end > 1000 * strftime('%s', 'now')"""
    )
    fun loadAllUpcomingLessons(): Observable<List<UpcomingLessonWithCourse>>
}