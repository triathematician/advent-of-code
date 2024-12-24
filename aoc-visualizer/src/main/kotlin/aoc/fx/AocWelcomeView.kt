package aoc.fx

import tornadofx.View
import tornadofx.label
import tornadofx.stackpane

class AocWelcomeView : View() {
    override val root = stackpane {
        label("Welcome to Advent of Code!")
    }
}

