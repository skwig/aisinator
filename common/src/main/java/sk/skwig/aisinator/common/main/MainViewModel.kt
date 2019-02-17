package sk.skwig.aisinator.common.main

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(
) : ViewModel() {

    private val stateRelay = BehaviorRelay.createDefault<ViewState>(ViewState.Dashboard)

    val state: Observable<ViewState>

    init {
        state = stateRelay.distinctUntilChanged()
    }

    fun onDashboardSelected() = stateRelay.accept(ViewState.Dashboard)

    fun onTimetableSelected() = stateRelay.accept(ViewState.Timetable)

    sealed class ViewState {
        object Dashboard : ViewState()
        object Timetable : ViewState()
    }
}