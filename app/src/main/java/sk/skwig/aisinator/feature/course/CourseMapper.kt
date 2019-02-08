package sk.skwig.aisinator.feature.course

import sk.skwig.aisinator.common.EntityMapper

class CourseMapper : EntityMapper<CourseEntity, Course>() {

    override fun toEntity(domainObject: Course): CourseEntity =
        CourseEntity(domainObject.id, domainObject.tag, domainObject.name, domainObject.time, domainObject.isActive)

    override fun fromEntity(entityObject: CourseEntity): Course =
        Course(entityObject.id, entityObject.tag, entityObject.name, entityObject.time, entityObject.isActive)
}

class CourseworkDeadlineMapper(private val courseMapper: CourseMapper) :
    EntityMapper<CourseworkDeadlineEntity, CourseworkDeadline>() {

    override fun toEntity(domainObject: CourseworkDeadline): CourseworkDeadlineEntity =
        CourseworkDeadlineEntity(
            domainObject.id,
            domainObject.course.id,
            domainObject.name,
            domainObject.deadline,
            domainObject.isOpen,
            domainObject.isDismissed
        )

    override fun fromEntity(entityObject: CourseworkDeadlineEntity): CourseworkDeadline {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun fromEntityRelation(coursesWithDeadlines: List<CourseWithDeadlines>): List<CourseworkDeadline> =
        coursesWithDeadlines
            .flatMap {
                courseMapper.fromEntity(it.course).let { course ->
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
}