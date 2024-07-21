package com.example.elkdocsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.elkdocsassignment.ui.theme.ElkDocsAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElkDocsAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            //.padding(16.dp)
    ) {
        // Left Sidebar: Lines move from top to bottom
        VerticalWavingSidebar(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(8.dp), // Adjusted width for larger gap
            lineCount = 3, // Adjusted line count
            lineColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan),
            direction = Direction.TopToBottom
        )


        // Right Sidebar: Lines move from bottom to top
        VerticalWavingSidebar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(8.dp), // Adjusted width for larger gap
            lineCount = 3, // Adjusted line count
            lineColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan),
            direction = Direction.BottomToTop
        )
    }
}

enum class Direction {
    TopToBottom,
    BottomToTop
}

@Composable
fun VerticalWavingSidebar(
    modifier: Modifier = Modifier,
    lineCount: Int = 8,
    lineColors: List<Color> = List(lineCount) { Color.Blue },
    direction: Direction = Direction.TopToBottom
) {
    val infiniteTransition = rememberInfiniteTransition()
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val colorTransition = rememberInfiniteTransition()
    val colorOffset by colorTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.fillMaxHeight()) {
        val waveHeight = 100f
        val waveLength = 150f
        val width = size.width
        val height = size.height
        val lineWidth = width / (lineCount + 2) // Increased the gap between lines
        val strokeWidth = 1.dp.toPx() // Thinner line width

        // Interpolating between colors
        val interpolatedColors = lineColors.mapIndexed { index, color ->
            val nextIndex = (index + 1) % lineColors.size
            val startColor = lineColors[index]
            val endColor = lineColors[nextIndex]
            val fraction = (colorOffset + index / lineCount.toFloat()) % 1f
            blendColor(startColor, endColor, fraction)
        }

        for (i in 0 until lineCount) {
            val xOffset = i * lineWidth

            val yOffset = when (direction) {
                Direction.TopToBottom -> height * waveOffset - waveHeight
                Direction.BottomToTop -> -height * waveOffset + height
            }

            val path = Path().apply {
                moveTo(xOffset, yOffset)
                for (y in 0 until height.toInt() + waveHeight.toInt() step waveHeight.toInt()) {
                    relativeQuadraticBezierTo(
                        -lineWidth * 0.5f, waveHeight,
                        0f, waveHeight
                    )
                    relativeQuadraticBezierTo(
                        lineWidth * 0.5f, waveHeight,
                        0f, waveHeight
                    )
                }
            }

            drawPath(
                path = path,
                color = interpolatedColors[i % interpolatedColors.size],
                style = Stroke(width = strokeWidth)  // Use thinner line width
            )
        }
    }
}

fun blendColor(startColor: Color, endColor: Color, fraction: Float): Color {
    val startRed = startColor.red
    val startGreen = startColor.green
    val startBlue = startColor.blue
    val startAlpha = startColor.alpha

    val endRed = endColor.red
    val endGreen = endColor.green
    val endBlue = endColor.blue
    val endAlpha = endColor.alpha

    val red = startRed + fraction * (endRed - startRed)
    val green = startGreen + fraction * (endGreen - startGreen)
    val blue = startBlue + fraction * (endBlue - startBlue)
    val alpha = startAlpha + fraction * (endAlpha - startAlpha)

    return Color(red, green, blue, alpha)
}