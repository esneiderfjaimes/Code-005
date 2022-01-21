package com.red.code005.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.red.code005.R
import com.red.code005.databinding.FragmentLoginBinding
import com.red.code005.toast
import com.red.code005.ui.PermissionsFragment

class LoginFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!PermissionsFragment.hasPermissions(context))
            navigate(LoginFragmentDirections.actionLoginToPermissions())
        if (FirebaseAuth.getInstance().currentUser != null)
            navigate(LoginFragmentDirections.actionLoginToHome())
    }

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    activity?.let { activity ->
                        loginViewModel.authWithGoogle(activity, it)
                    }
                }
            ))[LoginViewModel::class.java]
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginViewModel = loginViewModel
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loginResult.error?.let {
                    toast(it)
                }
                loginResult.success?.let {
                    navigate(LoginFragmentDirections.actionLoginToHome())
                }
            })
    }

    private fun navigate(@NonNull directions: NavDirections) {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}