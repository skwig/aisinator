package sk.skwig.aisinator.feature.course

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class CourseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCourseworkDeadlines(deadlines: List<CourseworkDeadlineEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCoursesWithDeadlines(
        courses: List<CourseEntity>,
        deadlines: List<CourseworkDeadlineEntity>
    ): Unit // TODO: zmenit na Completable ked to fixnut v Room libke

    fun insertCoursesWithDeadlines(coursesWithDeadlines: List<CourseWithDeadlines>): Completable {
        return Completable.fromAction {
            insertCoursesWithDeadlines(
                coursesWithDeadlines.map { it.course },
                coursesWithDeadlines.flatMap { it.deadlines }
            )
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCourse(course: CourseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCourses(courses: List<CourseEntity>): Completable

    @Query("SELECT * FROM courses")
    abstract fun loadAllCourses(): Observable<List<CourseEntity>>

    @Transaction
    @Query("SELECT * FROM courses")
    abstract fun loadAllCoursesWithDeadlines(): Observable<List<CourseWithDeadlines>>
}