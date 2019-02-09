package sk.skwig.aisinator.course.db.roomdao

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.course.db.entity.CourseEntity

@Dao
interface CourseRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourse(course: CourseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourses(courses: List<CourseEntity>): Completable

    @Query("SELECT * FROM courses")
    fun loadAllCourses(): Observable<List<CourseEntity>>

}