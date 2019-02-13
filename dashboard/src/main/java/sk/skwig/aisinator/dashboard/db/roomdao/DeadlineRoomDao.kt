package sk.skwig.aisinator.dashboard.db.roomdao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.dashboard.db.entity.CourseEntity
import sk.skwig.aisinator.dashboard.db.entity.DeadlineEntity
import sk.skwig.aisinator.dashboard.db.entity.DeadlineWithCourse

@Dao
interface DeadlineRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourseworkDeadlines(deadlines: List<DeadlineEntity>): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourseDeadlinesWithCourses(
        courses: List<CourseEntity>,
        deadlines: List<DeadlineEntity>
    ): Unit // TODO: zmenit na Completable ked to fixnu v Room libke

    @Update
    fun updateCourseworkDeadline(deadlineEntity: DeadlineEntity): Completable

    @Transaction
    @Query("SELECT * FROM deadlines d JOIN courses c ON c.course_id = d.fk_course_id WHERE d.deadline_is_dismissed == 0")
    fun loadAllCourseworkDeadlines(): Observable<List<DeadlineWithCourse>>
}