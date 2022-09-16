package com.example.materialwatch

import android.graphics.Color
import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem

class ColoredWatchFaceService : WatchFaceService(), DataClient.OnDataChangedListener {
    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = ColoredWatchCanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE,
        )

        return WatchFace(
            watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer,
        )
    }

    companion object {
        private const val PATH_COLORS = "/colors"
        private const val PATH_START_ACTIVITY = "/start-activity"
        private const val KEY_COLOR_HOUR = "key_color_hour"
        private const val KEY_COLOR_MINUTE = "key_color_minute"
        private const val KEY_COLOR_SECOND = "key_color_second"
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
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
}