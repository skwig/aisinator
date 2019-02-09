package sk.skwig.aisinator.feature.course.db

import sk.skwig.aisinator.common.EntityMapper
import sk.skwig.aisinator.common.SimpleEntityMapper
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import sk.skwig.aisinator.feature.course.db.entity.CourseEntity
import sk.skwig.aisinator.feature.course.db.entity.CourseworkDeadlineEntity
import sk.skwig.aisinator.feature.course.db.entity.CourseworkDeadlineWithCourse

class CourseMapper : SimpleEntityMapper<CourseEntity, Course>() {

    override fun toEntity(domain: Course): CourseEntity =
        CourseEntity(
            domain.id,
            domain.tag,
            domain.name,
            domain.time,
            domain.isActive
        )

    override fun fromEntity(entity: CourseEntity): Course =
        Course(entity.id, entity.tag, entity.name, entity.time, entity.isActive)

}

class CourseworkDeadlineMapper :
    EntityMapper<CourseworkDeadlineEntity, CourseworkDeadline>() {

    override fun toEntity(domain: CourseworkDeadline): CourseworkDeadlineEntity =
        CourseworkDeadlineEntity(
            domain.id,
            domain.course.id,
            domain.name,
            domain.deadline,
            domain.isOpen,
            domain.isDismissed
        )
}

class CourseworkDeadlineWithCourseMapper(
    private val courseMapper: CourseMapper,
    private val courseworkDeadlineMapper: CourseworkDeadlineMapper
) : SimpleEntityMapper<CourseworkDeadlineWithCourse, CourseworkDeadline>() {

    override fun toEntity(domain: CourseworkDeadline): CourseworkDeadlineWithCourse =
        CourseworkDeadlineWithCourse(
            courseworkDeadlineMapper.toEntity(domain),
            courseMapper.toEntity(domain.course)
        )

    override fun fromEntity(entity: CourseworkDeadlineWithCourse): CourseworkDeadline =
        CourseworkDeadline(
            entity.courseworkDeadline.id,
            courseMapper.fromEntity(entity.course),
            entity.courseworkDeadline.name,
            entity.courseworkDeadline.deadline,
            entity.courseworkDeadline.isOpen,
            entity.courseworkDeadline.isDismissed
        )
}