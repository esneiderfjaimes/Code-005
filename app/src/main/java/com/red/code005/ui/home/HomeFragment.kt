package com.red.code005.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.red.code005.R
import com.red.code005.databinding.FragmentHomeBinding
import com.red.code005.navigateTo
import com.red.code005.ui.home.camera.CameraFragment
import com.red.code005.ui.home.relations.RelationsFragment
import com.red.code005.ui.home.widgets.WidgetsFragment


private val PAGE_BUTTON_IDS = listOf(R.id.page_relations, R.id.page_camera, R.id.page_widgets)

class HomeFragment : NavHostFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (FirebaseAuth.getInstance().currentUser == null)
            navigateTo(HomeFragmentDirections.actionHomeToLogin())
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
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
            Glide.with(requireContext()).asBitmap()
                .load(FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
                .circleCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        topBar.menu[0].icon = BitmapDrawable(resources, resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // no use
                    }
                })
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
                    bottomNav.selectedItemId = PAGE_BUTTON_IDS[position]
                }
            })
            bottomNav.setOnItemSelectedListener { item ->
                topBar.title = item.title.toString()
                pager.currentItem = PAGE_BUTTON_IDS.indexOf(item.itemId)
                true
            }
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
}