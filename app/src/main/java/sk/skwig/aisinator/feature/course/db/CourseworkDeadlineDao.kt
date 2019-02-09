package sk.skwig.aisinator.feature.course.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import sk.skwig.aisinator.feature.course.db.roomdao.CourseworkDeadlineRoomDao

interface CourseworkDeadlineDao {
    fun insertCourseworkDeadlines(deadlines: List<CourseworkDeadline>): Completable
    fun updateCourseworkDeadline(deadline: CourseworkDeadline): Completable
    fun loadAllCourses(): Observable<List<CourseworkDeadline>>
}

class CourseworkDeadlineDaoImpl(
    private val dao: CourseworkDeadlineRoomDao,
    private val courseMapper: CourseMapper,
    private val courseworkDeadlineMapper: CourseworkDeadlineMapper,
    private val courseworkDeadlineWithCourseMapper: CourseworkDeadlineWithCourseMapper
) : CourseworkDeadlineDao {

    override fun insertCourseworkDeadlines(deadlines: List<CourseworkDeadline>): Completable {
        return Completable.fromAction {
            dao.insertCourseDeadlinesWithCourses(
                courseMapper.toEntityList(deadlines.map { it.course }),
                courseworkDeadlineMapper.toEntityList(deadlines)
            )
        }
    }

    override fun updateCourseworkDeadline(deadline: CourseworkDeadline): Completable {
        return dao.updateCourseworkDeadline(courseworkDeadlineMapper.toEntity(deadline))
    }

    override fun loadAllCourses(): Observable<List<CourseworkDeadline>> =
        dao.loadAllCourseworkDeadlines()
            .map { courseworkDeadlineWithCourseMapper.fromEntityList(it) }

}