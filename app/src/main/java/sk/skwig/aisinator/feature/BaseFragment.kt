package sk.skwig.aisinator.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding> : Fragment() {

    protected open val viewModel: VM by lazy { createViewModel() }
    protected open val binding: B by lazy { createBinding(layoutInflater, null) }
    protected open val navController: NavController by lazy { createNavController() }

    protected val disposable = CompositeDisposable()

    protected abstract fun createViewModel(): VM
    protected abstract fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): B
    open fun createNavController() = findNavController()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}