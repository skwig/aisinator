package sk.skwig.aisinator.dashboard.db.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.dashboard.db.entity.CourseEntity

@Dao
interface CourseRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourse(course: CourseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourses(courses: List<CourseEntity>): Completable

    @Query("SELECT * FROM courses")
    fun loadAllCourses(): Observable<List<CourseEntity>>

}