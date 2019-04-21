package sk.skwig.aisinator.feature.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.databinding.FragmentLoginBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.login.viewmodel.LoginViewModel
import timber.log.Timber

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun createViewModel() = Injector.injectLoginViewModel(this)

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.onLogin(binding.login.text.toString(), binding.password.text.toString())
        }

        viewModel.also {
            it.state.subscribeBy(
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
