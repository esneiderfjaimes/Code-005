package com.red.code005.ui.camera

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.annotation.UiThread
import com.red.code005.R
import com.red.code005.databinding.CameraPreviewBinding
import com.red.code005.utils.animations.bgColorAnim
import com.red.code005.utils.animations.drawableAnim

@UiThread
fun CameraFragment.bindPreview(resource: Drawable) {
    removePreview()
    cameraPreviewBinding = CameraPreviewBinding.inflate(
        LayoutInflater.from(requireContext()), binding.root, true
    ).apply {
        // Variables used for sending animation
        val colorsBackground: ColorStateList? = sendButton.backgroundTintList
        val colorsIcon: ColorStateList? = sendButton.imageTintList
        val bgColorAnim = sendButton.bgColorAnim(to = Color.TRANSPARENT)

        // Show captured photo in view
        imagePreview.setImageDrawable(resource)

        // Button listener used to send the image and display an animation
        sendButton.setOnClickListener {
            // TODO: Save and send File
            // Show sending animation
            sendButton.drawableAnim(R.drawable.avd_send_out) {
                // Invert background and icon colors
                sendButton.imageTintList = colorsBackground
                bgColorAnim.start()
                // Show loading animation
                sendButton.drawableAnim(R.drawable.avd_loading)
            }
            // if sending fails remove loading animation and show send icon
            // sendButton.imageTintList = ColorStateList.valueOf(colorsIcon.tint())
            // bgColorAnim.reverse()
            // sendButton.drawableAnim(R.drawable.avd_send_in)
        }

        closeButton.setOnClickListener {
            // TODO: Remove File
            closePreview()
        }
        saveButton.setOnClickListener {
            // TODO: Save File
            closePreview()
        }
    }
}

fun CameraFragment.closePreview() {
    removePreview()
    inProcess = false // End process
    reloadCameraControls() // Reload Controls
}

private fun CameraFragment.removePreview() {
    cameraPreviewBinding?.root?.let { binding.root.removeView(it) }
}