package sk.skwig.aisinator.util

import io.reactivex.Observable

fun <T> Observable<T>.replayLast() = this.replay(1)