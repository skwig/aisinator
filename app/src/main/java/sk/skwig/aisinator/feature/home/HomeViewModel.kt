package sk.skwig.aisinator.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val stateRelay = BehaviorRelay.createDefault<ViewState>(ViewState.Dashboard)

    val state: Observable<ViewState>

    init {
        state = stateRelay.distinctUntilChanged()
    }

    fun onDashboardSelected() {
        Log.d("matej", "onDashboardSelected() called")
        stateRelay.accept(ViewState.Dashboard)
    }

    fun onTimetableSelected() {
        Log.d("matej", "onTimetableSelected() called")
        stateRelay.accept(ViewState.Timetable)
    }

    sealed class ViewState {
        object Dashboard : ViewState()
        object Timetable : ViewState()
    }
}