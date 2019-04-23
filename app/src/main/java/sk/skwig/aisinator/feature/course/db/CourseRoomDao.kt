package sk.skwig.aisinator.feature.course.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CourseRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourse(course: CourseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourses(courses: List<CourseEntity>): Completable

    @Query("SELECT * FROM courses")
    fun loadAllCourses(): Observable<List<CourseEntity>>

    @Query("SELECT * FROM courses c LEFT JOIN lessons l ON c.course_id = l.fk_course_id WHERE l.fk_course_id IS NULL")
    fun loadAllCoursesWithoutLessons(): Observable<List<CourseEntity>>
}