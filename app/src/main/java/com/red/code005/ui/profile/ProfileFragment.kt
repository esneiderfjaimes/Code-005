package com.red.code005.ui.profile

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.palette.graphics.Target
import com.red.code005.R
import com.red.code005.databinding.FragmentProfileBinding
import com.red.code005.ui.profile.ProfileViewModel.Action.*
import com.red.code005.ui.profile.ProfileViewModel.ProfileEvent
import com.red.code005.ui.profile.ProfileViewModel.ProfileEvent.*
import com.red.code005.utils.*
import com.red.domain.ProfilePreview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    // region Fields

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val bQuery get() = _binding!!.includedCommonQuery

    private lateinit var username: String

    // endregion

    // region Override Methods & Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var lastPathSegment = activity?.intent?.data?.lastPathSegment
        if (lastPathSegment.isNullOrBlank()) {
            lastPathSegment = arguments?.getString("username", null)
            if (lastPathSegment.isNullOrBlank()) {
                toast(getString(R.string.profile_not_found))
                activity?.finish()
                return
            }
        }
        username = lastPathSegment.toString()
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(i, c, false).apply {
            val backTextUserName = "@${username}".repeat(75)
            backText.text = backTextUserName
            context?.animIn(backText, R.anim.rotation)
            topBar.title = username
            topBar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            viewModel.event.observe(
                viewLifecycleOwner,
                Observer(this@ProfileFragment::observerEvent)
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.consultProfile(username)
        bQuery.loading()
    }

    // endregion

    // region Private Methods

    private fun observerEvent(event: ProfileEvent) {
        when (event) {
            is ShowProfile -> showProfile(event.profilePreview)
            ProfileNotFound -> bQuery.message(R.string.profile_not_found)
            is ProfileError -> bQuery.error(event.exception)
            is ShowAction -> showAction(event.action)
            is ActionError -> bQuery.error(event.exception)
        }
    }

    private fun showProfile(profile: ProfilePreview) {
        viewModel.genAction(profile.id)
        binding.apply {
            requireContext().loadBitmapFromUrl(profile.photoUrl) { resource ->
                photo.setImageBitmap(resource)
                resource.generatePalette { palette ->
                    palette.getSwatchForTarget(Target.VIBRANT)?.let { paletteVibrant ->
                        val colorAnimation = ValueAnimator.ofObject(
                            ArgbEvaluator(),
                            context?.getColorStateList(R.color.T5colorSurface)?.defaultColor
                                ?: Color.RED, paletteVibrant.rgb
                        )
                        colorAnimation.duration = 1500
                        colorAnimation.addUpdateListener { animator ->
                            parentRelative.setBackgroundColor(animator.animatedValue as Int)
                        }
                        colorAnimation.start()
                    }
                }
            }
            name.text = profile.name
            state.text = profile.state
            bQuery.hide()
        }
    }

    private fun showAction(action: ProfileViewModel.Action) {
        binding.apply {
            progressLoading.visibility = View.GONE
            when (action) {
                CONNECT -> {
                    btnConnect.visibility = View.VISIBLE
                    btnConnect.setText(R.string.connect)
                }
                CONNECTED -> {
                    btnConnectedOrShare.visibility = View.VISIBLE
                    btnConnectedOrShare.setText(R.string.connected)
                }
                SHARE -> {
                    btnConnectedOrShare.visibility = View.VISIBLE
                    btnConnectedOrShare.setText(R.string.share)
                }
                NO_ACTION -> {
                    // NO action
                }
            }
        }
    }

    // endregion

}