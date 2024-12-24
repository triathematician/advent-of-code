package aoc.fx

import tornadofx.App
import tornadofx.launch

class AocApp : App(AocWorkspace::class)

fun main(args: Array<String>) = launch<AocApp>(args)