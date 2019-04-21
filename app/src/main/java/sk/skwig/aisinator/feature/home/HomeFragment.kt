package sk.skwig.aisinator.feature.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.R
import sk.skwig.aisinator.databinding.FragmentHomeBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun createViewModel() = Injector.injectHomeViewModel(this)

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun createNavController(): NavController {
        return activity?.findNavController(R.id.home_nav_host_fragment)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += viewModel.state.subscribeBy(
            onNext = {
                Log.d("matej", "onCreate() called $it")
                when (it) {
                    is HomeViewModel.ViewState.Dashboard -> navController.navigate(R.id.action_global_dashboardFragment)
                    is HomeViewModel.ViewState.Timetable -> navController.navigate(R.id.action_global_timetableFragment)
                    is HomeViewModel.ViewState.Chat-> navController.navigate(R.id.action_global_chatroomListFragment)
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
                R.id.menu_chat -> {
                    viewModel.onChatSelected()
                    true
                }
                else -> false
            }
        }
    }
}
