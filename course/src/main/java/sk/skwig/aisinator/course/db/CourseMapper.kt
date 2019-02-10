package sk.skwig.aisinator.course.db

import sk.skwig.aisinator.common.SimpleEntityMapper
import sk.skwig.aisinator.course.Course
import sk.skwig.aisinator.course.db.entity.CourseEntity

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