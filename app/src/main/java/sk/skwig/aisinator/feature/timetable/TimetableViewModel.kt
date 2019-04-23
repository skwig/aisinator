package sk.skwig.aisinator.feature.timetable

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.feature.lesson.Lesson
import sk.skwig.aisinator.feature.lesson.LessonRepository
import sk.skwig.aisinator.feature.settings.SettingsManager

// TODO: checkboxy na prednasky a cvicenia
// TODO: prazdne columny & rowy collapsnut nech nezaberaju tolko miesta

class TimetableViewModel(
    private val lessonRepository: LessonRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val openTimetableItemSelectionRelay = PublishRelay.create<Unit>()

    val openTimetableItemSelection: Observable<Unit>
        get() = openTimetableItemSelectionRelay

    private val filteringStrategyRelay = BehaviorRelay.createDefault<TimetableFilteringStrategy>(
        TypeTimetableFilteringStrategy(
            settingsManager.isTimetableShowingLectures,
            settingsManager.isTimetableShowingSeminars,
            settingsManager.isTimetableShowingCustom
        )
    )

    // TODO: live template na toto
    private val uiStateRelay = BehaviorRelay.create<ViewState>()

    val uiState: Observable<ViewState>
        get() = uiStateRelay

    private val disposable = CompositeDisposable()

    init {
        disposable += filteringStrategyRelay
            .map { it as TypeTimetableFilteringStrategy }
            .doOnNext {
                settingsManager.isTimetableShowingLectures = it.isShowingLectures
                settingsManager.isTimetableShowingSeminars = it.isShowingSeminars
                settingsManager.isTimetableShowingCustom = it.isShowingCustomItems
            }
            .distinctUntilChanged()
            .switchMap { filterState ->
                lessonRepository.getLessons()
                    .distinctUntilChanged()
                    .map { it.map { TimetableItem(it) } }
                    .map { it.filter { filterState.allows(it) } }
                    .map<ViewState> { ViewState.Normal(filterState, it) }
                    .onErrorReturn { ViewState.Error(filterState, it) }
                    .startWith(ViewState.Loading(filterState))
            }
            .subscribe(uiStateRelay)
    }

    fun onFilterTimetable(filterState: TimetableFilteringStrategy) {
        filteringStrategyRelay.accept(filterState)
    }

    fun onTimetableItemSelection() {
        openTimetableItemSelectionRelay.accept(Unit)
    }

    sealed class ViewState(val filterState: TypeTimetableFilteringStrategy) {
        class Loading(filterState: TypeTimetableFilteringStrategy) : ViewState(filterState)
        class Normal(filterState: TypeTimetableFilteringStrategy, val timetableItems: List<TimetableItem>) :
            ViewState(filterState)

        class Error(filterState: TypeTimetableFilteringStrategy, val throwable: Throwable) : ViewState(filterState)
    }
}

data class TypeTimetableFilteringStrategy(
    val isShowingLectures: Boolean = true,
    val isShowingSeminars: Boolean = true,
    val isShowingCustomItems: Boolean = true
) : TimetableFilteringStrategy {
    override fun allows(timetableItem: TimetableItem): Boolean {
        return timetableItem.lesson.let {

            val isItemType = when (it) {
                is Lesson.Seminar -> isShowingSeminars
                is Lesson.Lecture -> isShowingLectures
            }

            val isCustomItemAllowed = isShowingCustomItems && it.isCustom

            return@let isItemType || isCustomItemAllowed
        }
    }
}