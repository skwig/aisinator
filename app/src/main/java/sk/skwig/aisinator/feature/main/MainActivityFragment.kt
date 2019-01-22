package sk.skwig.aisinator.feature.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.databinding.FragmentMainBinding
import sk.skwig.aisinator.feature.main.viewmodel.MainViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.also {
            // subscribe
        }
    }
}
