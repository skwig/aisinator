package sk.skwig.aisinator.feature.course.db.roomdao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.feature.course.db.entity.CourseEntity
import sk.skwig.aisinator.feature.course.db.entity.CourseworkDeadlineEntity
import sk.skwig.aisinator.feature.course.db.entity.CourseworkDeadlineWithCourse

@Dao
interface CourseworkDeadlineRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourseworkDeadlines(deadlines: List<CourseworkDeadlineEntity>): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourseDeadlinesWithCourses(
        courses: List<CourseEntity>,
        deadlines: List<CourseworkDeadlineEntity>
    ): Unit // TODO: zmenit na Completable ked to fixnu v Room libke

    @Update
    fun updateCourseworkDeadline(courseworkDeadlineEntity: CourseworkDeadlineEntity): Completable

    @Transaction
    @Query("SELECT * FROM coursework_deadlines INNER JOIN courses ON courses.id = coursework_deadlines.course_id WHERE coursework_deadlines.is_dismissed == 0")
    fun loadAllCourseworkDeadlines(): Observable<List<CourseworkDeadlineWithCourse>>
}