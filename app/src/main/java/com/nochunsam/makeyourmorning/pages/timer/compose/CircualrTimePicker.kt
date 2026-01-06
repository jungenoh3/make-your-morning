package com.nochunsam.makeyourmorning.pages.timer.compose

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun CircularTimerPicker(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.size(320.dp),
    initialMinutes: Int = 10,
    onTimeChange: (Int) -> Unit
) {
    var minutes by remember { mutableIntStateOf(initialMinutes) }
    val colorScheme = MaterialTheme.colorScheme

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures { change, _ ->
                val center = size / 2
                val x = change.position.x - center.width
                val y = change.position.y - center.height

                var angle = Math.toDegrees(atan2(x, -y).toDouble())
                if (angle < 0) angle += 360

                val computed = ceil(angle / 6).toInt().coerceIn(1, 60)
                minutes = computed
                onTimeChange(computed)
            }
        }
    ) {
        val strokeWidth = 20.dp.toPx()
        val radius = min(size.width, size.height) / 2 - strokeWidth
        val diameter = radius * 2
        val topLeft = Offset(center.x - radius, center.y - radius)

        drawCircle(
            color = colorScheme.outlineVariant,
            radius = radius,
            style = Stroke(strokeWidth)
        )

        val sweep = minutes * 6f

        val angleRad = Math.toRadians(sweep - 90.0)
        val knobX = center.x + radius * cos(angleRad).toFloat()
        val knobY = center.y + radius * sin(angleRad).toFloat()

        drawArc(
            color = colorScheme.primary,
            startAngle = -90f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = topLeft,
            size = Size(diameter, diameter),
            style = Stroke(strokeWidth, cap = StrokeCap.Round)
        )

        drawCircle(
            color = colorScheme.primary,
            radius = strokeWidth * 0.9f,
            center = Offset(knobX, knobY)
        )

        // -----------------------------
        // Center minutes display here
        // -----------------------------
        val centerCircleRadius = radius * 0.45f

        drawCircle(
            color = colorScheme.background,
            radius = centerCircleRadius,
            center = center
        )
        val textColor = colorScheme.onSurface
        drawContext.canvas.nativeCanvas.apply {
            val text = String.format(Locale.ENGLISH, "%02d분", minutes)
            val paint = Paint().apply {
                color = textColor.toArgb()
                textSize = centerCircleRadius * 0.5f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            drawText(
                text,
                center.x,
                center.y - (paint.descent() + paint.ascent()) / 2,
                paint
            )
        }
    }
}
