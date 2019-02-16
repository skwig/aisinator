package sk.skwig.aisinator.dashboard.db.entity

import androidx.room.*

// naming convention:
// 1. prefixovat foreign keye "fk"
// 2. prefixovat columny nazvom tabulky
//      - room nezvlada duplikaty column namov v embednutom query resulte a prefix anotacia mu nestaci

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey
    @ColumnInfo(name = "course_id")
    val id: Long,

    @ColumnInfo(name = "course_tag")
    val tag: String,

    @ColumnInfo(name = "course_name")
    val name: String,

    @ColumnInfo(name = "course_time")
    val time: String,

    @ColumnInfo(name = "course_is_active")
    val isActive: Boolean
)
