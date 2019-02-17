package sk.skwig.aisinator.common.deadline.api

import org.jsoup.nodes.Document
import sk.skwig.aisinator.common.data.Course
import sk.skwig.aisinator.common.data.Deadline
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

interface DeadlineHtmlParser {
    fun parseDeadlines(document: Document): List<Deadline>
}

internal class DeadlineHtmlParserImpl : DeadlineHtmlParser {

    override fun parseDeadlines(document: Document): List<Deadline> {

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
}