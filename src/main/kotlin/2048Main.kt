import com.appjackstudio.game2048.Game2048
import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont

const val TILE_SIZE = 107.0
const val PADDING = 15.0
const val TOTAL_TILE_SIZE = TILE_SIZE + PADDING
const val TOTAL_BOARD_SIZE = TOTAL_TILE_SIZE * Game2048.BOARD_SIZE + PADDING
val TEXT_COLOR = ColorRGBa.fromHex(0x776e65)

val game = Game2048()

val gameTitle get() = "Jack Gordon's 2048 | Score: ${game.score}"

fun main() = application {
    configure {
        width = TOTAL_BOARD_SIZE.toInt()
        height = TOTAL_BOARD_SIZE.toInt()
        title = "Loading..."
    }

    program {
        val font = loadFont("https://ff.static.1001fonts.net/c/l/clear-sans.bold.ttf", 72.0)
        window.title = gameTitle
        var gameIsOver = false

        keyboard.keyDown.listen {
            if (!gameIsOver) {
                val direction = when (it.key) {
                    KEY_ARROW_UP -> Game2048.Direction.UP
                    KEY_ARROW_RIGHT -> Game2048.Direction.RIGHT
                    KEY_ARROW_DOWN -> Game2048.Direction.DOWN
                    KEY_ARROW_LEFT -> Game2048.Direction.LEFT
                    else -> null
                }
                if (direction != null) {
                    val didChange = game.slideAndMerge(direction)
                    if (didChange) {
                        game.placeRandomTile()
                    }
                    window.title = gameTitle
                }
                if(game.isGameOver){
                    gameIsOver = true;
                }
            }
        }

        extend {
            drawer.fontMap = font
            drawer.clear(ColorRGBa.fromHex(0xbbada0))

            for (x in 0 until Game2048.BOARD_SIZE) {
                for (y in 0 until Game2048.BOARD_SIZE) {
                    drawTile(drawer, x, y)
                }
            }

            if (gameIsOver) {
                drawer.fill = ColorRGBa(.8, .8, .8, .8)
                drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())

                drawer.fill = ColorRGBa.BLACK
                drawer.textCenter("Game Over", width / 2.0, height / 2.0 - 30)
                drawer.textCenter("Score: ${game.score}", width / 2.0, height / 2.0 + 30)
            }
        }
    }
}

fun getColor(value: Int): ColorRGBa = when (value) {
    0 -> ColorRGBa(238 / 255.0, 228 / 255.0, 218 / 255.0, 0.35)
    2 -> ColorRGBa.fromHex(0xeee4da)
    4 -> ColorRGBa.fromHex(0xede0c8)
    8 -> ColorRGBa.fromHex(0xf2b179)
    16 -> ColorRGBa.fromHex(0xf59563)
    32 -> ColorRGBa.fromHex(0xf67c5f)
    64 -> ColorRGBa.fromHex(0xf65e3b)
    128 -> ColorRGBa.fromHex(0xedcf72)
    256 -> ColorRGBa.fromHex(0xedcc61)
    1024 -> ColorRGBa.fromHex(0xedc53f)
    2048 -> ColorRGBa.fromHex(0xf9f6f2)
    else -> ColorRGBa.fromHex(0x3c3a32)
}

fun drawTile(drawer: Drawer, x: Int, y: Int) = drawer.isolated {
    val value = game[x, y]

    drawer.translate(x * TOTAL_TILE_SIZE + PADDING, y * TOTAL_TILE_SIZE + PADDING)

    drawer.stroke = null
    drawer.fill = getColor(value)
    drawer.rectangle(0.0, 0.0, TILE_SIZE, TILE_SIZE, 5.0)

    if (value > 0) {
        drawer.fill = if (value > 4) ColorRGBa.fromHex(0xf9f6f2) else TEXT_COLOR
        drawer.textCenter(value.toString(), TILE_SIZE / 2, TILE_SIZE / 2, TILE_SIZE - PADDING)
    }
}
