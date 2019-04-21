package sk.skwig.aisinator.common.util.listing

import androidx.annotation.StringRes
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

    protected fun Observable<List<T>>.toViewState(@StringRes title: Int): Observable<ViewState<T>> {
        return this.map<ViewState<T>> { ViewState.Displaying(title, it) }
            .onErrorReturn { ViewState.Error(title, it) }
            .startWith(ViewState.Loading(title))
    }

    sealed class ViewState<T>(@StringRes val title: Int) {
        class Loading<T>(@StringRes title: Int) : ViewState<T>(title)
        class Displaying<T>(@StringRes title: Int, val items: List<T>) : ViewState<T>(title)
        class Error<T>(@StringRes title: Int, val throwable: Throwable) : ViewState<T>(title)
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