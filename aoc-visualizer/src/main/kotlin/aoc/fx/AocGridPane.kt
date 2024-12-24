package aoc.fx

import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import tornadofx.Component
import tornadofx.ScopedInstance
import kotlin.math.max

/** JavaFx canvas for rendering a grid. */
class AocGridPane(val model: AocGridModel): Pane() {
    init {
        val scale = 1000.0 / max(model.wid, model.height)
        children.addAll(model.components(scale))
    }
}

class AocGridModel(var wid: Int = 10, var height: Int = 10, var x0: Int = 0, var y0: Int = 0) : Component(), ScopedInstance {
    val fills = mutableMapOf<Pair<Int, Int>, javafx.scene.paint.Color>()
    fun components(scale: Double): List<Node> {
        val children = mutableListOf<Node>()
        for (x in x0 until x0 + wid) {
            for (y in y0 until y0 + height) {
                val square = Rectangle((x - x0) * scale, (y - y0) * scale, scale, scale).apply {
                    fill = fills[x to y]
                    stroke = javafx.scene.paint.Color.BLACK
                    strokeWidth = scale / 50
                }
                children.add(square)
            }
        }
        children.add(Rectangle(0.0, 0.0, wid * scale, height * scale).apply {
            fill = null
            stroke = javafx.scene.paint.Color.BLACK
            strokeWidth = scale / 10
        })
        return children
    }

    fun autobounds() {
        val minx = fills.keys.minOf { it.first }
        val miny = fills.keys.minOf { it.second }
        val maxx = fills.keys.maxOf { it.first }
        val maxy = fills.keys.maxOf { it.second }
        x0 = minx
        y0 = miny
        wid = maxx - minx + 1
        height = maxy - miny + 1
    }
}