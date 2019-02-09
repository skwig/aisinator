package sk.skwig.aisinator.feature.course.db.roomdao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.feature.course.db.entity.CourseEntity

@Dao
interface CourseRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourse(course: CourseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourses(courses: List<CourseEntity>): Completable

    @Query("SELECT * FROM courses")
    fun loadAllCourses(): Observable<List<CourseEntity>>

}