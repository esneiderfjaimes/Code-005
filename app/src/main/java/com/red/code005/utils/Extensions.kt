package com.red.code005.utils

import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.red.code005.R

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(@StringRes resId: Int) {
    toast(requireContext().getString(resId))
}

fun Fragment.navigateTo(@NonNull directions: NavDirections) {
    lifecycleScope.launchWhenStarted {
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .navigate(directions)
    }
}