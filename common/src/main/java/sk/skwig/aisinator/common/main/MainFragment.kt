package sk.skwig.aisinator.common.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.common.R
import sk.skwig.aisinator.common.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java).also {
            disposable += it.state.subscribeBy(
                onNext = {
                    when (it) {
//                        MainViewModel.ViewState.Dashboard -> navController.navigate(R.id.action_mainFragment_to_dashboardFragment)
//                        MainViewModel.ViewState.Timetable -> navController.navigate(R.id.action_mainFragment_to_timetableFragment)
                    }
                },
                onError = Timber::e
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
