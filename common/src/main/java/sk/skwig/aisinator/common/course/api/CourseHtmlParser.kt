package sk.skwig.aisinator.common.course.api

import org.jsoup.nodes.Document
import sk.skwig.aisinator.common.data.Course
import sk.skwig.aisinator.common.data.Coursework
import sk.skwig.aisinator.common.data.CourseworkEntry

interface CourseHtmlParser {
    fun parseActiveCourses(document: Document): List<Course>
    fun parseCoursework(document: Document): Coursework
}

internal class CourseHtmlParserImpl : CourseHtmlParser {

    override fun parseActiveCourses(document: Document): List<Course> {
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

    override fun parseCoursework(document: Document): Coursework {
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
}