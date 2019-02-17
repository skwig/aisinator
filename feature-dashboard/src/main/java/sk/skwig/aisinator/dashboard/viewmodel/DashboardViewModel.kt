package sk.skwig.aisinator.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import sk.skwig.aisinator.dashboard.CourseRepository
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

}