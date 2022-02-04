package com.red.code005.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.red.code005.databinding.FragmentLoginBinding
import com.red.code005.ui.PermissionsFragment
import com.red.code005.ui.common.Event
import com.red.code005.ui.login.LoginViewModel.LoginNavigation.*
import com.red.code005.utils.navigateTo
import com.red.code005.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // region Fields

    private val viewModel: LoginViewModel by viewModels()

    // endregion

    // region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!PermissionsFragment.hasPermissions(context))
            navigateTo(LoginFragmentDirections.actionLoginToPermissions())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.result =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                activity?.let { activity ->
                    viewModel.authWithGoogle(activity, it)
                }
            }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?) =
        FragmentLoginBinding.inflate(i, c, false).apply {
            loginViewModel = viewModel
            lifecycleOwner = this@LoginFragment
            fragment = this@LoginFragment
            viewModel.events.observe(
                viewLifecycleOwner,
                Observer(this@LoginFragment::validateEvents)
            )
        }.root

    // endregion

    // region Private Methods

    private fun validateEvents(event: Event<LoginViewModel.LoginNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowError -> navigation.run {
                    this@LoginFragment.toast("Error -> ${error.message}")
                }
                is ShowToastError -> navigation.run {
                    this@LoginFragment.toast(resId)
                }
                SignIn -> {
                    navigateTo(LoginFragmentDirections.actionLoginToHome())
                }
            }
        }
    }

    // endregion

}