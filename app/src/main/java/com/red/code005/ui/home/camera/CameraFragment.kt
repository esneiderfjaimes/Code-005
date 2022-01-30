package com.red.code005.ui.home.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.red.code005.databinding.FragmentHomeCameraBinding

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private var _binding: FragmentHomeCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        _binding = FragmentHomeCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}