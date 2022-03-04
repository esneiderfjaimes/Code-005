package com.red.code005.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.red.code005.R
import com.red.code005.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: $this")
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent: $this")
        if (intent?.data?.host == "www.code005.com")
            findNavController(R.id.fragment_container).navigate(intent.data!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: $this")
    }

    companion object {
        const val TAG = "LOG:Main"

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File = context.run {
            val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else applicationContext.filesDir
        }
    }
}