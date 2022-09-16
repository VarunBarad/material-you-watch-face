package com.example.materialwatch

import android.app.Application
import android.graphics.Color

class WatchApplication : Application() {
    var colorHour: Int = Color.CYAN
    var colorMinute: Int = Color.LTGRAY
    var colorSecond: Int = Color.YELLOW
}