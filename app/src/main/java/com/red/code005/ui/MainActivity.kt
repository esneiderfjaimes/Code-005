package com.red.code005.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.red.code005.R
import com.red.code005.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
    }
}