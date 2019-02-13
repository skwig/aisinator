package sk.skwig.aisinator.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import sk.skwig.aisinator.dashboard.DashboardRepository
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    init {
        val a = dashboardRepository.getUpcomingLessons()
            .subscribe({
                Log.d("matej", "$it")
                Log.d("matej", "$it")
            })
    }

}