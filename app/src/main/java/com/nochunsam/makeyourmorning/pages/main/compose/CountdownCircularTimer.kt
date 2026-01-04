package com.nochunsam.makeyourmorning.pages.main.compose

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.nochunsam.makeyourmorning.utilities.block.FocusBlockingManager
import java.util.Locale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun CountdownCircularTimer(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.size(320.dp),
) {
    val remaining by FocusBlockingManager.remainingSeconds.collectAsState()
    val currentProgress by FocusBlockingManager.progress.collectAsState()
    val progress by animateFloatAsState(
        targetValue = currentProgress,
        label = "progress"
    )

    val colorScheme = MaterialTheme.colorScheme
    val minutes = remaining / 60
    val seconds = remaining % 60
    val display = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)

    Canvas(modifier = modifier) {
        val strokeWidth = 20.dp.toPx()
        val radius = min(size.width, size.height) / 2 - strokeWidth
        val diameter = radius * 2
        val topLeft = Offset(center.x - radius, center.y - radius)

        // Base circle
        drawCircle(
            color = colorScheme.outlineVariant,
            radius = radius,
            style = Stroke(strokeWidth)
        )

        // Progress arc
        drawArc(
            color = colorScheme.primary,
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(diameter, diameter),
            style = Stroke(strokeWidth, cap = StrokeCap.Round)
        )

        // Knob
        val angleRad = Math.toRadians(progress * 360 - 90.0)
        val knobX = center.x + radius * cos(angleRad).toFloat()
        val knobY = center.y + radius * sin(angleRad).toFloat()

        drawCircle(
            color = colorScheme.primary,
            radius = strokeWidth * 0.9f,
            center = Offset(knobX, knobY)
        )

        val textColor = colorScheme.onSurface
        drawContext.canvas.nativeCanvas.apply {
            val paint = Paint().apply {
                color = textColor.toArgb()
                textSize = radius * 0.4f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            drawText(
                display,
                center.x,
                center.y - (paint.descent() + paint.ascent()) / 2,
                paint
            )
        }
    }
}
