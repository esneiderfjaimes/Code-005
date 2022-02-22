package com.red.code005.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.red.code005.databinding.FragmentLoginBinding
import com.red.code005.ui.login.LoginViewModel.LoginEvent
import com.red.code005.ui.login.LoginViewModel.LoginEvent.*
import com.red.code005.utils.navigateTo
import com.red.code005.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // region Fields

    private val viewModel: LoginViewModel by viewModels()
    private var needInflate = true

    // endregion

    // region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "onDetach: $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isLogin.invoke()) {
            observerEvent(SignIn)
            needInflate = false
            return
        }
        viewModel.result =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                activity?.let { activity ->
                    viewModel.authWithGoogle(activity, it)
                }
            }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?) = if (needInflate)
        FragmentLoginBinding.inflate(i, c, false).apply {
            loginViewModel = viewModel
            lifecycleOwner = this@LoginFragment
            fragment = this@LoginFragment
            viewModel.event.observe(
                viewLifecycleOwner,
                Observer(this@LoginFragment::observerEvent)
            )
        }.root else null

    // endregion

    // region Private Methods

    private fun observerEvent(event: LoginEvent) {
        when (event) {
            is ShowError -> "Error -> ${event.error.message}"
            is ShowToastError -> toast(event.resId)
            SignIn -> navigateTo(LoginFragmentDirections.actionLoginToHome())
        }
    }

    // endregion

    companion object {
        val TAG = "LOG:Login"
    }
}