package com.example.materialwatch

import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.google.android.gms.wearable.*
import kotlinx.coroutines.*

class ColorListenerService : WearableListenerService(), DataClient.OnDataChangedListener {
    companion object {
        private const val PATH_COLORS = "/colors"
        private const val PATH_START_ACTIVITY = "/start-activity"
        private const val KEY_COLOR_HOUR = "key_color_hour"
        private const val KEY_COLOR_MINUTE = "key_color_minute"
        private const val KEY_COLOR_SECOND = "key_color_second"
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        PATH_COLORS -> {
                            val dataMap = DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                            val colorHour = dataMap.getInt(KEY_COLOR_HOUR, Color.CYAN)
                            val colorMinute = dataMap.getInt(KEY_COLOR_MINUTE, Color.LTGRAY)
                            val colorSecond = dataMap.getInt(KEY_COLOR_SECOND, Color.YELLOW)

                            (this.application as WatchApplication).also {
                                it.colorHour = colorHour
                                it.colorMinute = colorMinute
                                it.colorSecond = colorSecond
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            PATH_START_ACTIVITY -> {
                val intent = Intent(this, WearMainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}