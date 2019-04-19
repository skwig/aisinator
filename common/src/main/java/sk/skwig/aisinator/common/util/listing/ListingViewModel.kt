package sk.skwig.aisinator.common.util.listing

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class ListingViewModel<T> : ViewModel() {

    protected val stateRelay = BehaviorRelay.create<ViewState<T>>()

    val state: Observable<ViewState<T>>
        get() = stateRelay

    protected val disposable = CompositeDisposable()

    protected fun Observable<List<T>>.toViewState(): Observable<ViewState<T>> {
        return this.map<ViewState<T>> { ViewState.Displaying(it) }
            .onErrorReturn { ViewState.Error(it) }
            .startWith(ViewState.Loading())
    }

    sealed class ViewState<T> {
        class Loading<T> : ViewState<T>()
        data class Displaying<T>(val items: List<T>) : ViewState<T>()
        data class Error<T>(val throwable: Throwable) : ViewState<T>()
    }
}

abstract class DismissableListingViewModel<T> : ListingViewModel<T>() {
    open fun onDismiss(item: T) {
        disposable += dismissCompletable(item)
            .subscribeOn(Schedulers.io())
            .subscribe({}, Timber::e)
    }

    abstract fun dismissCompletable(item: T): Completable
}