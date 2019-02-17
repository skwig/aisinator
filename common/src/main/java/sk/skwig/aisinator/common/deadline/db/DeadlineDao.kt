package sk.skwig.aisinator.common.deadline.db

import io.reactivex.Completable
import io.reactivex.Observable
import sk.skwig.aisinator.common.data.db.mapper.CourseMapper
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.data.db.mapper.DeadlineMapper
import sk.skwig.aisinator.common.data.db.mapper.DeadlineWithCourseMapper

interface DeadlineDao {
    fun insertDeadlines(deadlines: List<Deadline>): Completable
    fun updateDeadline(deadline: Deadline): Completable
    fun loadAllCourses(): Observable<List<Deadline>>
}

internal class DeadlineDaoImpl(
    private val dao: DeadlineRoomDao,
    private val courseMapper: CourseMapper,
    private val deadlineMapper: DeadlineMapper,
    private val deadlineWithCourseMapper: DeadlineWithCourseMapper
) : DeadlineDao {

    override fun insertDeadlines(deadlines: List<Deadline>): Completable {
        return Completable.fromAction {
            dao.insertCourseDeadlinesWithCourses(
                courseMapper.toEntityList(deadlines.map { it.course }),
                deadlineMapper.toEntityList(deadlines)
            )
        }
    }

    override fun updateDeadline(deadline: Deadline): Completable {
        return dao.updateCourseworkDeadline(deadlineMapper.toEntity(deadline))
    }

    override fun loadAllCourses(): Observable<List<Deadline>> =
        dao.loadAllCourseworkDeadlines()
            .map { deadlineWithCourseMapper.fromEntityList(it) }

}