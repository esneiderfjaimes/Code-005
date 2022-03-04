package com.red.code005.ui.home

import android.animation.ValueAnimator
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
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.red.code005.R
import com.red.code005.R.drawable.*
import com.red.code005.databinding.FragmentHomeBinding
import com.red.code005.ui.relations.ConnectionsFragment
import com.red.code005.ui.widgets.WidgetsFragment
import com.red.code005.utils.animations.cornerAnim
import com.red.code005.utils.animations.drawableAnim
import com.red.code005.utils.extensions.inRailFAB
import com.red.code005.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

private val PAGE_BUTTON_IDS = listOf(R.id.page_connections, R.id.page_widgets)

@AndroidEntryPoint
class HomeFragment : NavHostFragment(), NavigationBarView.OnItemSelectedListener {

    // region Fields

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fabAction: FloatingActionButton
    private var actionCornerAnim: ValueAnimator? = null
    private var noAnim = true

    private var pagerCallbacks = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (!noAnim) {
                fabAction.drawableAnim(if (position == 0) avd_add_to_camera else avd_camera_to_add)
                if (position == 0) actionCornerAnim?.reverse() else actionCornerAnim?.start()
            } else {
                noAnim = false
                fabAction.setImageResource(if (position == 0) ic_camera else ic_add)
            }

            binding.apply {
                bottomNav?.apply { selectedItemId = PAGE_BUTTON_IDS[position] } // Portrait
                navigationRail?.apply { selectedItemId = PAGE_BUTTON_IDS[position] } // Landscape
            }
        }
    }

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
        savedInstanceState: Bundle?,
    ): View {
        noAnim = true // don't animate when fragment is created and returning from another fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.moveTaskToBack(true)
        }
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            topBar.setOnMenuItemClickListener { menu ->  // Portrait Account
                if (menu.itemId == R.id.page_account) {
                    navigateTo(HomeFragmentDirections.actionHomeToAccount())
                    true
                } else false
            }
            navigationRail?.headerView?.inRailFAB()?.fabAccount?.apply { // Landscape Account
                setOnClickListener { navigateTo(HomeFragmentDirections.actionHomeToAccount()) }
            }

            viewModel.onUserPhoto(requireContext()) { bitmap ->
                if (topBar.menu.size > 0) // Portrait Account Bitmap
                    topBar.menu[0].icon = BitmapDrawable(resources, bitmap)
                navigationRail?.headerView?.inRailFAB()?.fabAccount?.apply { // Landscape Bitmap
                    setImageBitmap(bitmap)
                    imageTintList = null
                }
            }

            fabAction?.let { this@HomeFragment.fabAction = it } // Portrait Action
            navigationRail?.headerView?.inRailFAB {
                this@HomeFragment.fabAction = fabAction  // Landscape Action
            }
            this@HomeFragment.fabAction.setOnClickListener {
                if (pager.currentItem == 0) {
                    navigateTo(HomeFragmentDirections.actionHomeToCamera())
                }
            }
            actionCornerAnim = this@HomeFragment.fabAction.cornerAnim(0.3f)

            bottomNav?.setOnItemSelectedListener(this@HomeFragment) // Portrait Nav
            navigationRail?.setOnItemSelectedListener(this@HomeFragment) // Landscape Nav

            pager.adapter = PagerAdapter(this@HomeFragment)
            pager.registerOnPageChangeCallback(pagerCallbacks)

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.topBar.title = item.title.toString()
        binding.pager.currentItem = PAGE_BUTTON_IDS.indexOf(item.itemId)
        return true
    }

    // endregion

    // region Inner Classes & Interfaces

    private inner class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = PAGE_BUTTON_IDS.size
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ConnectionsFragment()
                else -> WidgetsFragment()
            }
        }
    }

    // endregion

    companion object {
        const val TAG = "LOG:Home"
    }
}