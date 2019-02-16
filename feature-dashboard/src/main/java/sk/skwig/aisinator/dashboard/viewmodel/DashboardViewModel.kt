package sk.skwig.aisinator.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import sk.skwig.aisinator.dashboard.DashboardRepository
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

}