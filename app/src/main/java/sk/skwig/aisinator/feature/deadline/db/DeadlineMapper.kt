package sk.skwig.aisinator.feature.deadline.db

import sk.skwig.aisinator.feature.EntityMapper
import sk.skwig.aisinator.feature.SimpleEntityMapper
import sk.skwig.aisinator.feature.course.db.CourseMapper
import sk.skwig.aisinator.feature.deadline.Deadline

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