package sk.skwig.aisinator.common.course.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.common.data.Course
import sk.skwig.aisinator.common.data.db.mapper.CourseMapper

interface CourseDao {
    fun insertCourse(course: Course): Completable
    fun insertCourses(courses: List<Course>): Completable
    fun loadAllCourses(): Observable<List<Course>>
}

internal class CourseDaoImpl(
    private val dao: CourseRoomDao,
    private val mapper: CourseMapper
) : CourseDao {

    override fun insertCourse(course: Course): Completable = dao.insertCourse(mapper.toEntity(course))

    override fun insertCourses(courses: List<Course>): Completable = dao.insertCourses(mapper.toEntityList(courses))

    override fun loadAllCourses(): Observable<List<Course>> = dao.loadAllCourses().map { mapper.fromEntityList(it) }

}