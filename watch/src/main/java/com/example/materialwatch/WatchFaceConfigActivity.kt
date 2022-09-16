package com.example.materialwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.materialwatch.databinding.ActivityWatchFaceConfigBinding

class WatchFaceConfigActivity : ComponentActivity() {
    private lateinit var binding: ActivityWatchFaceConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWatchFaceConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}