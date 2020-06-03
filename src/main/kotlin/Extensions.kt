import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.shapes.RoundedRectangle
import org.openrndr.text.writer

fun Drawer.textCenter(text: String, x: Double, y: Double) = writer {
    val font = fontMap ?: return@writer

    val height = font.height
    val width = textWidth(text)

    text(text, x - (width / 2), y + (height / 2))
}

fun Drawer.rectangle(x: Double, y: Double, w: Double, h: Double, r: Double) {
    contour(RoundedRectangle(x, y, w, h, r).contour)
}

fun ColorRGBa.Companion.fromCSSRGBa(r: Int, g: Int, b: Int, a: Double): ColorRGBa =
    ColorRGBa(r / 255.0, g / 255.0, b / 255.0, a)
