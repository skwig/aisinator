package sk.skwig.aisinator.feature.lesson.db

import sk.skwig.aisinator.feature.EntityMapper
import sk.skwig.aisinator.feature.SimpleEntityMapper
import sk.skwig.aisinator.feature.course.db.CourseMapper
import sk.skwig.aisinator.feature.lesson.*

// TODO: cleanup v lessonWithCourse mapperoch

class LessonMapper(private val lessonTimeMapper: LessonTimeMapper) :
    EntityMapper<LessonEntity, Lesson>() {

    override fun toEntity(domain: Lesson): LessonEntity =
        LessonEntity(
            domain.course.id,
            domain.room,
            domain.teacher,
            domain is Lesson.Lecture,
            lessonTimeMapper.toEntity(domain.startTime),
            lessonTimeMapper.toEntity(domain.endTime)
        )
}

class LessonTimeMapper : SimpleEntityMapper<LessonTimeEntity, LessonTime>() {
    override fun toEntity(domain: LessonTime): LessonTimeEntity =
        LessonTimeEntity(
            domain.dayOfWeek,
            domain.time
        )

    override fun fromEntity(entity: LessonTimeEntity): LessonTime =
        LessonTime(
            entity.dayOfWeek,
            entity.time
        )
}

class LessonWithCourseMapper(
    private val courseMapper: CourseMapper,
    private val lessonMapper: LessonMapper,
    private val lessonTimeMapper: LessonTimeMapper
) : SimpleEntityMapper<LessonWithCourseEntity, Lesson>() {
    override fun toEntity(domain: Lesson): LessonWithCourseEntity {
        return LessonWithCourseEntity(lessonMapper.toEntity(domain), courseMapper.toEntity(domain.course))
    }

    override fun fromEntity(entity: LessonWithCourseEntity): Lesson {
        return entity.let {
            if (it.lesson.isLecture) {
                Lesson.Lecture(
                    courseMapper.fromEntity(it.course),
                    lessonTimeMapper.fromEntity(it.lesson.startTime),
                    lessonTimeMapper.fromEntity(it.lesson.endTime),
                    it.lesson.teacher,
                    it.lesson.room
                )
            } else {
                Lesson.Seminar(
                    courseMapper.fromEntity(it.course),
                    lessonTimeMapper.fromEntity(it.lesson.startTime),
                    lessonTimeMapper.fromEntity(it.lesson.endTime),
                    it.lesson.teacher,
                    it.lesson.room
                )
            }
        }
    }
}

class UpcomingLessonMapper(
    private val lessonWithCourseMapper: LessonWithCourseMapper
) : SimpleEntityMapper<UpcomingLessonEntity, UpcomingLesson>() {
    override fun fromEntity(entity: UpcomingLessonEntity): UpcomingLesson {
        return UpcomingLesson(
            lessonWithCourseMapper.fromEntity(entity.lessonWithCourse),
            entity.startTime,
            entity.endTime
        )
    }

    override fun toEntity(domain: UpcomingLesson) = TODO()
}