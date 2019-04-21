package sk.skwig.aisinator.feature.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import sk.skwig.aisinator.databinding.FragmentDashboardBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.dashboard.viewmodel.DashboardViewModel

class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {
    override fun createViewModel(): DashboardViewModel {
        return Injector.injectDashboardViewModel(this)
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(layoutInflater, container, false)
    }
}
