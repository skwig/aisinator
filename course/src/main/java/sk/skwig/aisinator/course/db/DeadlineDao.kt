package sk.skwig.aisinator.course.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.course.Deadline
import sk.skwig.aisinator.course.db.roomdao.DeadlineRoomDao

interface DeadlineDao {
    fun insertCourseworkDeadlines(deadlines: List<Deadline>): Completable
    fun updateCourseworkDeadline(deadline: Deadline): Completable
    fun loadAllCourses(): Observable<List<Deadline>>
}

class DeadlineDaoImpl(
    private val dao: DeadlineRoomDao,
    private val courseMapper: CourseMapper,
    private val deadlineMapper: DeadlineMapper,
    private val deadlineWithCourseMapper: DeadlineWithCourseMapper
) : DeadlineDao {

    override fun insertCourseworkDeadlines(deadlines: List<Deadline>): Completable {
        return Completable.fromAction {
            dao.insertCourseDeadlinesWithCourses(
                courseMapper.toEntityList(deadlines.map { it.course }),
                deadlineMapper.toEntityList(deadlines)
            )
        }
    }

    override fun updateCourseworkDeadline(deadline: Deadline): Completable {
        return dao.updateCourseworkDeadline(deadlineMapper.toEntity(deadline))
    }

    override fun loadAllCourses(): Observable<List<Deadline>> =
        dao.loadAllCourseworkDeadlines()
            .map { deadlineWithCourseMapper.fromEntityList(it) }

}