package sk.skwig.aisinator.common

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.common.di.util.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM
    protected lateinit var binding: B

    protected val disposable = CompositeDisposable()

    open val navController: NavController
        get() = findNavController()

    abstract fun createViewModel() : VM

    @CallSuper
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
    }
}