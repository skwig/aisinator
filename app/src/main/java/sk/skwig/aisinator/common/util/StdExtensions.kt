package sk.skwig.aisinator.common.util

fun <T> MutableList<T>.setAll(list: List<T>): Boolean {
    clear()
    return addAll(list)
}