package sk.skwig.aisinator.util

import okhttp3.ResponseBody
import org.jsoup.Jsoup

fun ResponseBody.toDocument() = Jsoup.parse(this.string())