package sk.skwig.aisinator.common

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.skwig.aisinator.di.util.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VM : androidx.lifecycle.ViewModel, B : ViewDataBinding> : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: VM
    lateinit var binding: B

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}