package sk.skwig.aisinator.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import sk.skwig.aisinator.di.util.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel, B : ViewDataBinding> : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: VM
    lateinit var binding: B

    abstract val navController: NavController

    protected val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}