package com.red.code005.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.red.code005.databinding.FragmentWidgetsBinding

class WidgetsFragment : Fragment() {

    private lateinit var widgetsViewModel: WidgetsViewModel
    private var _binding: FragmentWidgetsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        widgetsViewModel = ViewModelProvider(this)[WidgetsViewModel::class.java]
        _binding = FragmentWidgetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}