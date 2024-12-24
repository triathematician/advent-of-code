module triathematician.aocvisualizer {
    requires javafx.controls;
    requires kotlin.stdlib;
    requires tornadofx;
    requires de.jensd.fx.glyphs.fontawesome;
    requires transitive triathematician.aoc.aoc2016;

    exports aoc.fx;
    exports aoc.fx.e2016;
}