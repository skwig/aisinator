package sk.skwig.aisinator.feature.course

import sk.skwig.aisinator.common.RelationEntityMapper
import sk.skwig.aisinator.common.SimpleEntityMapper

class CourseMapper : SimpleEntityMapper<CourseEntity, Course>() {

    override fun toEntity(domain: Course): CourseEntity =
        CourseEntity(domain.id, domain.tag, domain.name, domain.time, domain.isActive)

    override fun fromEntity(entity: CourseEntity): Course =
        Course(entity.id, entity.tag, entity.name, entity.time, entity.isActive)

}

class CourseworkDeadlineMapper(private val courseMapper: CourseMapper) :
    RelationEntityMapper<CourseworkDeadlineEntity, CourseWithDeadlines, CourseworkDeadline>() {

    override fun toEntity(domain: CourseworkDeadline): CourseworkDeadlineEntity =
        CourseworkDeadlineEntity(
            domain.id,
            domain.course.id,
            domain.name,
            domain.deadline,
            domain.isOpen,
            domain.isDismissed
        )

    override fun toRelationList(domains: List<CourseworkDeadline>): List<CourseWithDeadlines> =
        domains
            .groupBy { it.course }
            .map { CourseWithDeadlines(courseMapper.toEntity(it.key), toEntityList(it.value)) }

    override fun fromRelationList(relations: List<CourseWithDeadlines>): List<CourseworkDeadline> =
        relations
            .flatMap {
                val course = courseMapper.fromEntity(it.course)
                it.deadlines.map {
                    CourseworkDeadline(
                        it.id,
                        course,
                        it.name,
                        it.deadline,
                        it.isOpen,
                        it.isDismissed
                    )
                }
            }
}