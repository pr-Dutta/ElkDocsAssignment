Waving Sidebar Animation in Jetpack Compose

This project demonstrates how to create a continuously waving vertical line animation on a sidebar in a Jetpack Compose-based Android application. 
The animation features two sidebars with different wave directions and colors, providing a visually appealing effect.

Features
* Left Sidebar: Lines move from top to bottom.
* Right Sidebar: Lines move from bottom to top.
* Customizable Line Colors: Choose from a variety of colors.
* Smooth Animation: Uses infinite transitions for continuous waving effect.

Video Link:- https://drive.google.com/file/d/1Ftfm4vylLO1S0Z0X6HmeNhRsZ6b90cJ2/view?usp=sharing

Dependencies
  Jetpack Compose: Ensure you have the latest version of Jetpack Compose set up in your project.
  Kotlin: Used for writing the composable functions.

Code Overview
  MainScreen Composable
  Displays the main screen with two sidebars. Each sidebar uses the VerticalWavingSidebar composable.

"""""""""""""""""""""""

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Left Sidebar: Lines move from top to bottom
        VerticalWavingSidebar(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(8.dp),
            lineCount = 3,
            lineColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan),
            direction = Direction.TopToBottom
        )

        // Right Sidebar: Lines move from bottom to top
        VerticalWavingSidebar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(8.dp),
            lineCount = 3,
            lineColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan),
            direction = Direction.BottomToTop
        )
    }
}

"""""""""""""""""""""""

VerticalWavingSidebar Composable
  Creates the waving animation using a Canvas and Path. Supports customizable line count, colors, and direction.

"""""""""""""""""""""""
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
        val lineWidth = width / (lineCount + 2)
        val strokeWidth = 1.dp.toPx()

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
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

"""""""""""""""""""""""

Utilities
  Color Blending Function: Interpolates between two colors based on a fraction.

""""""""""""""""""""""

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

""""""""""""""""""""""

Setup

1. Clone the Repository:
git clone https://github.com/pr-Dutta/ElkDocsAssignment.git
cd ElkDocsAssignment

2. Install Dependencies:
Make sure you have the necessary dependencies in your build.gradle file.

3. Run the Project:
Use Android Studio to build and run the project on an Android device or emulator.

Contributing
* Contributions are welcome! Feel free to fork the repository and submit pull requests.




