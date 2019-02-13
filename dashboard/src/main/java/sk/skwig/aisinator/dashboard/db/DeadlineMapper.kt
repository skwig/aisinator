package sk.skwig.aisinator.dashboard.db

import sk.skwig.aisinator.common.EntityMapper
import sk.skwig.aisinator.common.SimpleEntityMapper
import sk.skwig.aisinator.dashboard.Deadline
import sk.skwig.aisinator.dashboard.db.entity.DeadlineWithCourse
import sk.skwig.aisinator.dashboard.db.entity.DeadlineEntity

class DeadlineMapper :
    EntityMapper<DeadlineEntity, Deadline>() {

    override fun toEntity(domain: Deadline): DeadlineEntity =
        DeadlineEntity(
            domain.id,
            domain.course.id,
            domain.name,
            domain.deadline,
            domain.isOpen,
            domain.isDismissed
        )
}

class DeadlineWithCourseMapper(
    private val courseMapper: CourseMapper,
    private val deadlineMapper: DeadlineMapper
) : SimpleEntityMapper<DeadlineWithCourse, Deadline>() {

    override fun toEntity(domain: Deadline): DeadlineWithCourse =
        DeadlineWithCourse(
            deadlineMapper.toEntity(domain),
            courseMapper.toEntity(domain.course)
        )

    override fun fromEntity(entity: DeadlineWithCourse): Deadline =
        Deadline(
            entity.deadline.id,
            courseMapper.fromEntity(entity.course),
            entity.deadline.name,
            entity.deadline.deadline,
            entity.deadline.isOpen,
            entity.deadline.isDismissed
        )
}