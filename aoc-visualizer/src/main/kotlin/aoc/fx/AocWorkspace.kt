package aoc.fx

import aoc.fx.e2016.View201601
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.paint.Color
import tornadofx.*

class AocWorkspace : Workspace() {
    init {
        with (leftDrawer) {
            group("2024", FontAwesomeIcon.TREE.graphic.forestGreen) {
                label("No views ready for 2024.")
            }
            group("2016", FontAwesomeIcon.TREE.graphic.forestGreen) {
                hyperlink("Day 1") {
                    action {
                        isVisited = false
                        dock<View201601>()
                    }
                }
            }
        }

        primaryStage.width = 1200.0
        primaryStage.height = 800.0
        dock<AocWelcomeView>()
    }

    //region LAYOUT

    private fun Drawer.group(title: String, icon: Node? = null, op: EventTarget.() -> Unit) {
        item(title, icon, expanded = false) {
            op()
        }.apply {
            if (children.isEmpty()) {
//                removeFromParent()
            }
        }
    }

    //endregion
}

//region ICONS

private fun icon(icon: FontAwesomeIcon) = FontAwesomeIconView(icon)

private val FontAwesomeIcon.graphic
    get() = icon(this)

private val FontAwesomeIconView.forestGreen
    get() = apply {
        fill = Color(34.0/255, 139.0/255, 34.0/255, 1.0)
    }

//endregion