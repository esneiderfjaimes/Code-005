package com.red.code005.ui.relations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.red.code005.databinding.FragmentConnectionsBinding

class ConnectionsFragment : Fragment() {

    private lateinit var relationsViewModel: RelationsViewModel
    private var _binding: FragmentConnectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        relationsViewModel = ViewModelProvider(this)[RelationsViewModel::class.java]
        _binding = FragmentConnectionsBinding.inflate(inflater, container, false)
        binding.apply {
            count++
            val infoFragment =
                this@ConnectionsFragment.toString().replace("} (", "}\n(") +
                        "\n\nCount:$count"
            info.text = infoFragment
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var count = 0
    }
}