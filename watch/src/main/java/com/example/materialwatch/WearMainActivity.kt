package com.example.materialwatch

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.materialwatch.databinding.ActivityWearMainBinding
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem

class WearMainActivity : Activity() {
    private lateinit var binding: ActivityWearMainBinding
    private val watchApplication: WatchApplication by lazy { applicationContext as WatchApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWearMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cyan.setOnClickListener {
            watchApplication.colorHour = Color.CYAN
            watchApplication.colorMinute = Color.LTGRAY
            watchApplication.colorSecond = Color.YELLOW
        }

        binding.grey.setOnClickListener {
            watchApplication.colorHour = Color.LTGRAY
            watchApplication.colorMinute = Color.YELLOW
            watchApplication.colorSecond = Color.CYAN
        }

        binding.yellow.setOnClickListener {
            watchApplication.colorHour = Color.YELLOW
            watchApplication.colorMinute = Color.CYAN
            watchApplication.colorSecond = Color.LTGRAY
        }
    }
}