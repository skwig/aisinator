package sk.skwig.aisinator.common.data.db.mapper

import sk.skwig.aisinator.common.EntityMapper
import sk.skwig.aisinator.common.SimpleEntityMapper
import sk.skwig.aisinator.common.data.Lesson
import sk.skwig.aisinator.common.data.LessonTime
import sk.skwig.aisinator.common.data.UpcomingLesson
import sk.skwig.aisinator.common.data.db.LessonEntity
import sk.skwig.aisinator.common.data.db.LessonTimeEntity
import sk.skwig.aisinator.common.data.db.UpcomingLessonWithCourse

// TODO: cleanup v lesson mapperoch

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

class UpcomingLessonWithCourseMapper(
    private val courseMapper: CourseMapper,
    private val lessonMapper: LessonMapper,
    private val lessonTimeMapper: LessonTimeMapper
) : SimpleEntityMapper<UpcomingLessonWithCourse, UpcomingLesson>() {
    override fun fromEntity(entity: UpcomingLessonWithCourse): UpcomingLesson {

        val lesson = entity.let {
            if (it.upcomingLesson.lesson.isLecture) {
                Lesson.Lecture(
                    courseMapper.fromEntity(it.course),
                    lessonTimeMapper.fromEntity(it.upcomingLesson.lesson.startTime),
                    lessonTimeMapper.fromEntity(it.upcomingLesson.lesson.endTime),
                    it.upcomingLesson.lesson.teacher,
                    it.upcomingLesson.lesson.room
                )
            } else {
                Lesson.Seminar(
                    courseMapper.fromEntity(it.course),
                    lessonTimeMapper.fromEntity(it.upcomingLesson.lesson.startTime),
                    lessonTimeMapper.fromEntity(it.upcomingLesson.lesson.endTime),
                    it.upcomingLesson.lesson.teacher,
                    it.upcomingLesson.lesson.room
                )
            }
        }

        return UpcomingLesson(
            lesson,
            entity.upcomingLesson.startTime,
            entity.upcomingLesson.endTime
        )
    }

    override fun toEntity(domain: UpcomingLesson): UpcomingLessonWithCourse {
        throw RuntimeException("not needed")
    }
}