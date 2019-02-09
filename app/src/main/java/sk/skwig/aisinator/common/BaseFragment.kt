package sk.skwig.aisinator.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.di.util.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: VM
    lateinit var binding: B
    lateinit var navController: NavController

    protected val disposable = CompositeDisposable()

    @CallSuper
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }
}