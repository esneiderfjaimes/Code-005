package com.red.code005.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.red.code005.R
import com.red.code005.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            auth = FirebaseAuth.getInstance()
            signOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                lifecycleScope.launchWhenStarted {
                    Navigation.findNavController(requireActivity(), R.id.fragment_container)
                        .navigate(HomeFragmentDirections.actionHomeToLogin())
                }
            }
            return root
        }
    }
}