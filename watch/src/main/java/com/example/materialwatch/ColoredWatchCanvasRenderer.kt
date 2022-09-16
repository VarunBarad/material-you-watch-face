package com.example.materialwatch

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.core.graphics.withRotation
import androidx.core.graphics.withScale
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.time.Duration
import java.time.ZonedDateTime

// Default for how long each frame is displayed at expected frame rate
private const val FRAME_PERIOD_MS_DEFAULT: Long = 32L

class ColoredWatchCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int,
) : Renderer.CanvasRenderer2<ColoredWatchCanvasRenderer.ColoredSharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    FRAME_PERIOD_MS_DEFAULT,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false,
) {
    companion object {
        private const val WATCH_HAND_SCALE = 1.0f
    }

    class ColoredSharedAssets : SharedAssets {
        override fun onDestroy() {}
    }

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val clockHandPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth =
            context.resources.getDimensionPixelSize(R.dimen.clock_hand_stroke_width).toFloat()
    }
    private val outerElementPaint = Paint().apply {
        isAntiAlias = true
    }

    private lateinit var hourHandFill: Path
    private lateinit var minuteHandFill: Path
    private lateinit var secondHandFill: Path

    private var currentWatchFaceSize = Rect(0, 0, 0, 0)

    override suspend fun createSharedAssets(): ColoredSharedAssets {
        return ColoredSharedAssets()
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: ColoredSharedAssets
    ) {
        val backgroundColor = Color.BLACK
        canvas.drawColor(backgroundColor)

        drawClockHands(canvas, bounds, zonedDateTime)
    }

    private fun drawClockHands(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        if (currentWatchFaceSize != bounds) {
            currentWatchFaceSize = bounds
            recalculateClockHands(bounds)
        }

        val secondOfDay = zonedDateTime.toLocalTime().toSecondOfDay()

        val secondsPerHourHandRotation = Duration.ofHours(12).seconds
        val secondsPerMinuteHandRotation = Duration.ofHours(1).seconds

        val hourRotation = secondOfDay.rem(secondsPerHourHandRotation) * 360.0f / secondsPerHourHandRotation
        val minuteRotation = secondOfDay.rem(secondsPerMinuteHandRotation) * 360.0f / secondsPerMinuteHandRotation

        canvas.withScale(
            x = WATCH_HAND_SCALE,
            y = WATCH_HAND_SCALE,
            pivotX = bounds.exactCenterX(),
            pivotY = bounds.exactCenterY(),
        ) {
            val drawAmbient = renderParameters.drawMode == DrawMode.AMBIENT

            clockHandPaint.style = if (drawAmbient) {
                Paint.Style.STROKE
            } else {
                Paint.Style.FILL
            }

            withRotation(hourRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                clockHandPaint.color = (context as WatchApplication).colorHour
                drawPath(hourHandFill, clockHandPaint)
            }

            withRotation(minuteRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                clockHandPaint.color = (context as WatchApplication).colorMinute
                drawPath(minuteHandFill, clockHandPaint)
            }

            if (!drawAmbient) {
                val secondsPerSecondHandRotation = Duration.ofMinutes(1).seconds
                val secondsRotation = secondOfDay.rem(secondsPerSecondHandRotation) * 360.0f / secondsPerSecondHandRotation

                clockHandPaint.color = (context as WatchApplication).colorMinute
                withRotation(secondsRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                    drawPath(secondHandFill, clockHandPaint)
                }
            }
        }
    }

    private fun recalculateClockHands(bounds: Rect) {
        hourHandFill = createClockHand(
            bounds,
            0.21028f,
            0.02336f,
            (0.01869f + 0.03738f / 2.0f),
            1.5f,
            1.5f,
        )

        minuteHandFill = createClockHand(
            bounds,
            0.3783f,
            0.0163f,
            (0.01869f + 0.03738f / 2.0f),
            1.5f,
            1.5f,
        )

        secondHandFill = createClockHand(
            bounds,
            0.37383f,
            0.00934f,
            (0.01869f + 0.03738f / 2.0f),
            1.5f,
            1.5f,
        )
    }

    private fun createClockHand(
        bounds: Rect,
        length: Float,
        thickness: Float,
        gapBetweenHandAndCenter: Float,
        roundedCornerXRadius: Float,
        roundedCornerYRadius: Float
    ): Path {
        val width = bounds.width()
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()
        val left = centerX - thickness / 2 * width
        val top = centerY - (gapBetweenHandAndCenter + length) * width
        val right = centerX + thickness / 2 * width
        val bottom = centerY - gapBetweenHandAndCenter * width
        val path = Path()

        if (roundedCornerXRadius != 0.0f || roundedCornerYRadius != 0.0f) {
            path.addRoundRect(
                left,
                top,
                right,
                bottom,
                roundedCornerXRadius,
                roundedCornerYRadius,
                Path.Direction.CW
            )
        } else {
            path.addRect(
                left,
                top,
                right,
                bottom,
                Path.Direction.CW
            )
        }
        return path
    }

    private fun drawTopMiddleCircle(
        canvas: Canvas,
        bounds: Rect,
        radiusFraction: Float,
        gapBetweenOuterCircleAndBorderFraction: Float,
    ) {
        outerElementPaint.style = Paint.Style.FILL_AND_STROKE

        val centerX = 0.5f * bounds.width().toFloat()
        val centerY = bounds.width() * (gapBetweenOuterCircleAndBorderFraction + radiusFraction)

        canvas.drawCircle(
            centerX,
            centerY,
            radiusFraction * bounds.width(),
            outerElementPaint,
        )
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: ColoredSharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }

    override fun onDestroy() {
        scope.cancel("ColoredWatchFaceCanvasRenderer scope clear() request")
        super.onDestroy()
    }
}