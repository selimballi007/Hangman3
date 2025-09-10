import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun NotebookBackground(
    modifier: Modifier = Modifier,
    step: Float = 60f, // kare boyutu
    lineColor: Color = Color(0xFF90A4AE).copy(alpha = 0.5f) // gri-mavi, %50 şeffaf
) {
    Canvas(modifier = modifier) {
        // Dikey çizgiler
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = lineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 0.8f // biraz ince
            )
            x += step
        }

        // Yatay çizgiler
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 0.8f
            )
            y += step
        }
    }
}

