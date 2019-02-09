package sk.skwig.aisinator.feature.course.api

import org.jsoup.nodes.Document
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.course.Coursework
import sk.skwig.aisinator.feature.course.CourseworkDeadline
import sk.skwig.aisinator.feature.course.CourseworkEntry
import java.time.format.DateTimeFormatter

class CourseHtmlParser {

    fun parseActiveCourses(document: Document): List<Course> {
        return document
            .select("div#base table#tmtab_1 tbody tr[onmouseover]")
            .map {
                val columns = it.select("td")

                val courseElement = columns[0].select("a")
                val timeElement = columns[1]

                val courseText = courseElement.text()

                val id = courseElement.attr("href").substringAfterLast("predmet=").toLong()
                val tag = courseText.substringBefore(" ")
                val name = courseText.substringAfter(" ")
                val time = timeElement.text()

                Course(id, tag, name, time, true)
            }
    }

    fun parseCoursework(document: Document): Coursework {
        val entries = document.select("div#base b + table")
            .map {
                val nameElement = it.previousElementSibling()

                val headerElements = it.select("thead th")
                val valueElements = it.select("tbody td")

                val name = nameElement.text()
                val values = headerElements
                    .zip(valueElements)
                    .associate { it.first.text() to it.second.text() }

                CourseworkEntry(name, values)
            }

        return Coursework(entries)
    }

    fun parseCourseworkDeadlines(document: Document): List<CourseworkDeadline> {

        val dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/y H:m")

        return document
            // TODO: selectovat z otvorenych miest odovzdani
            .select("div#base table#tmtab_1 tbody tr")
            .map { deadlineElement ->
                val columns = deadlineElement.select("td.odsazena")

                val id = columns.last().selectFirst("a").attr("href").substringAfterLast("odevzdavarna=").toLong()
                val course = columns[0].selectFirst("a").let {
                    val id = it.attr("href").substringAfterLast("predmet=").toLong()
                    val text = it.text()
                    val tag = text.substringBefore(" ")
                    val name = text.substringAfter(" ")

                    // TODO: oddelit time od course
                    Course(
                        id,
                        tag,
                        name,
                        "",
                        true /* TODO: dat time a isActive prec */
                    )
                }
                val name = columns[1].text()
                val deadlineText = columns[4].text()
                val isOpen = columns[6].let {
                    when (it.selectFirst("img").attr("sysid")) {
                        "bullet-red-big-new" -> false
                        "bullet-green-big-new" -> true
                        else -> throw IllegalStateException()
                    }
                }
                val deadline = dateTimeFormatter.parse(deadlineText)
                CourseworkDeadline(id, course, name, deadlineText, isOpen)
            }
    }
}