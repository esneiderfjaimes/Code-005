package com.red.code005.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.red.code005.navigateTo

private val PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

/**
 * The sole purpose of this fragment is to request permissions and, once granted,
 * display the login fragment to the user.
 */
class PermissionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // check if the necessary permissions exist
        if (hasPermissions(requireContext(), *PERMISSIONS_REQUIRED)) {
            Log.i(
                "PermissionsLogs",
                "onCreate: nice! go to login"
            )
            navigateTo(PermissionsFragmentDirections.actionPermissionsToLogin())
            return
        }

        // if the permissions do not exist, start the request
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            var result = true
            map.entries.forEach { entry ->
                if (!entry.value) {
                    Log.w(
                        "PermissionsLogs",
                        "onCreate: oh no!, permission ${entry.key} has been denied"
                    )
                    result = false
                    return@forEach
                }
            }
            if (result) navigateTo(PermissionsFragmentDirections.actionPermissionsToLogin())
            // TODO: what to show when denying permissions?
            else activity?.finish()
        }.launch(PERMISSIONS_REQUIRED)
    }

    companion object {
        /**
         * Convenience method used to check if all permissions required by this app are granted
         */
        fun hasPermissions(
            context: Context,
            vararg permissions: String = PERMISSIONS_REQUIRED
        ): Boolean = permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
