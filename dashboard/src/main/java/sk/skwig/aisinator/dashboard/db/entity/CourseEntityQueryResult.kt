package sk.skwig.aisinator.dashboard.db.entity

import androidx.room.Embedded

// join je potrebne robit rucne
data class CourseworkDeadlineWithCourse(

    @Embedded
    val deadline: DeadlineEntity,

    @Embedded
    val course: CourseEntity
)