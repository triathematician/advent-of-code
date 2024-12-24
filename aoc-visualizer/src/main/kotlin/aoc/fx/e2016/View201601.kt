package aoc.fx.e2016

import aoc.fx.AocDayView
import aoc.fx.AocGrid
import aoc.fx.AocGridModel
import javafx.scene.paint.Color
import tornadofx.label
import tornadofx.vbox

class View201601: AocDayView() {
    val model: AocGridModel by inject()

    // set up model
    init {
        val path = aoc.aoc2016.AocDay1().vis1()
        model.fills.putAll(path.associateWith { Color.SANDYBROWN })
        model.fills.put(0 to 0, Color.GREEN)
        model.fills.put(path.last(), Color.RED)
        model.autobounds()
    }

    override val root = vbox {
        label("AOC 2016 Day 1")
        add(AocGrid())
    }
}