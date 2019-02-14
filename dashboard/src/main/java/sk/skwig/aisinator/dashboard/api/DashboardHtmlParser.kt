package sk.skwig.aisinator.dashboard.api

import org.jsoup.nodes.Document
import sk.skwig.aisinator.dashboard.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DashboardHtmlParser {

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

    fun parseDeadlines(document: Document): List<Deadline> {

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

                // TODO: Date manager
                val deadline = dateTimeFormatter.parse(deadlineText)
                val instant = LocalDateTime.from(deadline).toInstant(ZoneOffset.UTC)
                Deadline(id, course, name, instant, isOpen)
            }
    }

    fun parseLessons(document: Document): List<Lesson> {

        val lessonList = mutableListOf<Lesson>()
        val timeFormatter = DateTimeFormatter.ofPattern("H.mm")


        return document
            .select("div#base div.mainpage table")
            .let { timetableElement ->
                val headers = timetableElement.select("thead th").drop(1)

                val days = timetableElement.select("tr:has(td.zahlavi)")
                    .forEach {

                        // TODO: skraslit

                        val todaysLessons = it.children().drop(1)

                        // TODO: mapovat podla indexu a nie hodnoty? (pre language support)
                        val todaysDayOfWeek = when (it.select("td.zahlavi").text()) {
                            "Mon" -> DayOfWeek.MONDAY
                            "Tue" -> DayOfWeek.TUESDAY
                            "Wed" -> DayOfWeek.WEDNESDAY
                            "Thu" -> DayOfWeek.THURSDAY
                            "Fri" -> DayOfWeek.FRIDAY
                            "Sat" -> DayOfWeek.SATURDAY
                            "Sun" -> DayOfWeek.SUNDAY
                            else -> throw IllegalStateException("unknown day type")
                        }

                        var headerIndex = 0
                        for (lesson in todaysLessons) {
                            if (lesson.className().isBlank()) {
                                headerIndex++
                            } else {
                                val startIndex = headerIndex
                                headerIndex += lesson.attr("colspan").toInt()
                                val endIndex = headerIndex - 1

                                val startTime = LocalTime.parse(
                                    headers[startIndex].text().split("-").first(),
                                    timeFormatter
                                )
                                val endTime = LocalTime.parse(
                                    headers[endIndex].text().split("-").last(),
                                    timeFormatter
                                )

                                val startLessonTime = LessonTime(todaysDayOfWeek, startTime)
                                val endLessonTime = LessonTime(todaysDayOfWeek, endTime)

                                val links = lesson.select("a")

                                val room = links[0].text().substringBeforeLast(" ")
                                val course = links[1].let {
                                    val id = it.attr("href").substringAfter("predmet=").substringBefore(";").toLong()
                                    val name = it.text()
                                    Course(id, "UNKNOWN", name, "", true)
                                }
                                val teacher = links[2].text()

                                lessonList += when (lesson.className()) {
                                    "rozvrh-pred" ->
                                        Lesson.Lecture(
                                            course,
                                            startLessonTime,
                                            endLessonTime,
                                            teacher,
                                            room
                                        )
                                    "rozvrh-cvic" ->
                                        Lesson.Seminar(
                                            course,
                                            startLessonTime,
                                            endLessonTime,
                                            teacher,
                                            room
                                        )
                                    else -> throw IllegalStateException("unknown upcomingLesson type")
                                }
                            }

                        }


                    }
                lessonList
            }
    }
}