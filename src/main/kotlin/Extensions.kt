import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.shapes.RoundedRectangle
import org.openrndr.text.writer

fun Drawer.textCenter(text: String, x: Double, y: Double, maxWidth: Double = Double.NaN) = writer {
    val font = fontMap ?: return@writer

    val height = font.height
    val width = textWidth(text)

    isolated {
        translate(x, y)

        if (!maxWidth.isNaN()) {
            // If the text is too wide, scale it down
            if (width > maxWidth) {
                scale(maxWidth / width);
            }
        }

        text(text, -width / 2, height / 2)
    }
}

fun Drawer.rectangle(x: Double, y: Double, w: Double, h: Double, r: Double) {
    contour(RoundedRectangle(x, y, w, h, r).contour)
}

fun ColorRGBa.Companion.fromCSSRGBa(r: Int, g: Int, b: Int, a: Double): ColorRGBa =
    ColorRGBa(r / 255.0, g / 255.0, b / 255.0, a)
