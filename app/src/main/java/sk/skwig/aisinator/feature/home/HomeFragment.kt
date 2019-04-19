package sk.skwig.aisinator.feature.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.R
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val navController: NavController
        get() = activity?.findNavController(R.id.home_nav_host_fragment)!!

    override fun createViewModel() = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += viewModel.state.subscribeBy(
            onNext = {
                Log.d("matej", "onCreate() called $it")
                when (it) {
                    is HomeViewModel.ViewState.Dashboard -> navController.navigate(R.id.action_global_dashboardFragment)
                    is HomeViewModel.ViewState.Timetable -> navController.navigate(R.id.action_global_timetableFragment)
                }
            },
            onError = Timber::e
        )

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_dashboard -> {
                    viewModel.onDashboardSelected()
                    true
                }
                R.id.menu_timetable -> {
                    viewModel.onTimetableSelected()
                    true
                }
                else -> false
            }
        }
    }
}
