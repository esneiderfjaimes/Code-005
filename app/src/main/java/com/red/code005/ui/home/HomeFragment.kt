package com.red.code005.ui.home

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.red.code005.R
import com.red.code005.databinding.FragmentHomeBinding
import com.red.code005.ui.home.HomeViewModel.HomeEvent
import com.red.code005.ui.home.HomeViewModel.HomeEvent.GoSignIn
import com.red.code005.ui.home.HomeViewModel.HomeEvent.ShowError
import com.red.code005.ui.home.camera.CameraFragment
import com.red.code005.ui.home.relations.RelationsFragment
import com.red.code005.ui.home.widgets.WidgetsFragment
import com.red.code005.utils.navigateTo
import com.red.code005.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private val PAGE_BUTTON_IDS = listOf(R.id.page_relations, R.id.page_camera, R.id.page_widgets)

@AndroidEntryPoint
class HomeFragment : NavHostFragment(), NavigationBarView.OnItemSelectedListener {

    // region Fields

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.moveTaskToBack(true)
        }
        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewModel.event.observe(
                viewLifecycleOwner,
                Observer(this@HomeFragment::observerEvent)
            )
            viewModel.onUserPhoto(requireContext()) {
                if (topBar.menu.size > 0)
                    topBar.menu[0].icon = BitmapDrawable(resources, it)
                if (navigationRail?.headerView != null)
                    (navigationRail.headerView as FloatingActionButton).apply {
                        setImageBitmap(it)
                        imageTintList = null
                        setOnClickListener {
                            navigateTo(HomeFragmentDirections.actionHomeToAccount())
                        }
                    }
            }
            topBar.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.page_account -> {
                        navigateTo(HomeFragmentDirections.actionHomeToAccount())
                        true
                    }
                    else -> false
                }
            }
            pager.adapter = PagerAdapter(this@HomeFragment)
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (bottomNav != null)
                        bottomNav.selectedItemId = PAGE_BUTTON_IDS[position]
                    if (navigationRail != null)
                        navigationRail.selectedItemId = PAGE_BUTTON_IDS[position]
                }
            })
            bottomNav?.setOnItemSelectedListener(this@HomeFragment)
            navigationRail?.setOnItemSelectedListener(this@HomeFragment)
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        _binding?.topBar?.title = item.title.toString()
        _binding?.pager?.currentItem = PAGE_BUTTON_IDS.indexOf(item.itemId)
        return true
    }

    // endregion

    // region Private Methods

    private fun observerEvent(event: HomeEvent) {
        when (event) {
            is ShowError -> toast("Error -> ${event.error.message}")
            GoSignIn -> {
            }
        }
    }

    // endregion

    // region Inner Classes & Interfaces

    private inner class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = PAGE_BUTTON_IDS.size
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RelationsFragment()
                1 -> CameraFragment()
                else -> WidgetsFragment()
            }
        }
    }

    // endregion

    companion object {
        const val TAG = "LOG:Home"
    }
}