package sk.skwig.aisinator.feature.lesson.api

import org.jsoup.nodes.Document
import sk.skwig.aisinator.feature.course.Course
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.LessonTime
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface LessonHtmlParser {
    fun parseLessons(document: Document): List<Lesson>
}

internal class LessonHtmlParserImpl : LessonHtmlParser {

    override fun parseLessons(document: Document): List<Lesson> {

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

                                val startLessonTime =
                                    LessonTime(todaysDayOfWeek, startTime)
                                val endLessonTime =
                                    LessonTime(todaysDayOfWeek, endTime)

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