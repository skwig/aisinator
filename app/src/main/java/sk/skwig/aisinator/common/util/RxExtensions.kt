package sk.skwig.aisinator.common.util

import io.reactivex.Completable
import io.reactivex.Observable

fun <T> Observable<T>.replayLast() = this.replay(1)


inline fun <T> Observable<T>.completableOnNext(crossinline f: (T) -> Completable): Observable<T> {
    return this.concatMap {
        f(it).andThen(Observable.just(it))
    }
}