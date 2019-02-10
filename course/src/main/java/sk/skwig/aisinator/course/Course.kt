package sk.skwig.aisinator.course

data class Course(
    val id: Long,
    val tag: String,
    val name: String,
    val time: String,
    val isActive: Boolean
    /*attendance*/
)

data class Coursework(
    val entries: List<CourseworkEntry>
)

data class CourseworkEntry(
    val name: String,
    val values: Map<String, String>
)
