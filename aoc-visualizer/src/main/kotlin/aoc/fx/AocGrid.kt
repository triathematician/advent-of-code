package aoc.fx

import tornadofx.Component
import tornadofx.ScopedInstance
import tornadofx.View

/** Renders a grid of given size on a panel. */
class AocGrid: View() {
    val model: AocGridModel by inject()
    override val root = AocGridPane(model)
}