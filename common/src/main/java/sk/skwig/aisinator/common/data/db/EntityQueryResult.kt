package sk.skwig.aisinator.common.data.db

import androidx.room.Embedded

// join je potrebne robit rucne
data class DeadlineWithCourse(

    @Embedded
    val deadline: DeadlineEntity,

    @Embedded
    val course: CourseEntity
)

// join je potrebne robit rucne
data class UpcomingLessonWithCourse(

    @Embedded
    val upcomingLesson: UpcomingLessonEntity,

    @Embedded
    val course: CourseEntity
)