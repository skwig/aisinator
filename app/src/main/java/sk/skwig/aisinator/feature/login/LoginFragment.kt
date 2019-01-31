package sk.skwig.aisinator.feature.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.databinding.FragmentLoginBinding
import sk.skwig.aisinator.feature.login.viewmodel.LoginViewModel
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.onLogin(binding.login.text.toString(), binding.password.text.toString())
        }

        viewModel.also {
            // subscribe
            it.state
                .subscribeBy(
                    onNext = {

                        Log.d("matej", "LoginFragment.state $it")

                        binding.loadingOverlay.visibility =
                                if (it == LoginViewModel.State.LOADING) View.VISIBLE else View.GONE

                        when (it) {
                            LoginViewModel.State.ERROR -> Toast.makeText(
                                context,
                                "Failed to log in",
                                Toast.LENGTH_LONG
                            ).show()
                            LoginViewModel.State.SUCCESS -> {
                                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // TODO: spravna navigacia
                            }
                        }
                    },
                    onError = Timber::e
                )
        }

    }
}
