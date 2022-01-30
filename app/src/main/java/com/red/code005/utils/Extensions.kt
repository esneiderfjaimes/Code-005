package com.red.code005

import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.red.code005.ui.MainActivity

val Context.app: MainActivity
    get() = applicationContext as MainActivity

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

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this.viewModelStore, viewModelFactory)[T::class.java]
}