package sk.skwig.aisinator.feature.course.db

import sk.skwig.aisinator.feature.SimpleEntityMapper
import sk.skwig.aisinator.feature.course.Course

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
        Course(
            entity.id,
            entity.tag,
            entity.name,
            entity.time,
            entity.isActive
        )
}