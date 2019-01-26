package sk.skwig.aisinator.feature.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import sk.skwig.aisinator.R
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.databinding.FragmentDashboardBinding
import sk.skwig.aisinator.feature.main.viewmodel.DashboardViewModel
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java).also {
            it.showLogin.subscribe({ navController.navigate(R.id.action_global_loginFragment) }, Timber::e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            //            viewModel.doFoo()
        }
    }
}
