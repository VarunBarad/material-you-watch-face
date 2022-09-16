package com.example.materialwatch

import android.app.Application
import android.graphics.Color

class WatchApplication : Application() {
    var colorHour: Int = Color.parseColor("#D0BCFF")
    var colorMinute: Int = Color.parseColor("#CCC2DC")
    var colorSecond: Int = Color.parseColor("#4F378B")
}