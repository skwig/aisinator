package sk.skwig.aisinator.feature.timetable

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.LessonRepository

// TODO: checkboxy na prednasky a cvicenia
// TODO: prazdne columny & rowy collapsnut nech nezaberaju tolko miesta

class TimetableViewModel(private val lessonRepository: LessonRepository) : ViewModel() {

    // TODO: live template na toto
    private val filterStateRelay = BehaviorRelay.createDefault(TimetableFilterState())
    private val uiStateRelay = BehaviorRelay.create<ViewState>()

    val uiState: Observable<ViewState>
        get() = uiStateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += filterStateRelay
            .distinctUntilChanged()
            .switchMap { filterState ->
                lessonRepository.getLessons()
                    .distinctUntilChanged()
                    .doOnNext { Log.d("matej", "itemz $it") }
                    .map { it.map { TimetableItem(it) } }
                    .map { it.filter { filterState.allows(it) } }
                    .map<ViewState> { ViewState.Normal(filterState, it) }
                    .onErrorReturn { ViewState.Error(filterState, it) }
                    .startWith(ViewState.Loading(filterState))
            }
            .subscribe(uiStateRelay)
    }

    fun onFilterChanged(filterState: TimetableFilterState) {
        filterStateRelay.accept(filterState)
    }

    sealed class ViewState(val filterState: TimetableFilterState) {
        class Loading(filterState: TimetableFilterState) : ViewState(filterState)
        class Normal(filterState: TimetableFilterState, val timetableItems: List<TimetableItem>) :
            ViewState(filterState)

        class Error(filterState: TimetableFilterState, val throwable: Throwable) : ViewState(filterState)
    }
}

data class TimetableFilterState(
    val isShowingLectures: Boolean = true,
    val isShowingSeminars: Boolean = true,
    val isShowingCustomItems: Boolean = true
) {
    fun allows(timetableItem: TimetableItem): Boolean {
        return timetableItem.lesson.let {
            val isItemAllowed = true // filterState.isShowingCustomItems == it.isCustom

            return@let isItemAllowed && when (it) {
                is Lesson.Seminar -> isShowingSeminars
                is Lesson.Lecture -> isShowingLectures
            }
        }
    }
}