package com.example.materialwatch

import android.app.Activity
import android.os.Bundle
import com.example.materialwatch.databinding.ActivityWearMainBinding

class WearMainActivity : Activity() {
    private lateinit var binding: ActivityWearMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWearMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}