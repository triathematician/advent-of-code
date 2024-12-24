module triathematician.aoc.aoc.common {
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.kotlin;

    opens aoc.report to com.fasterxml.jackson.databind;

    exports aoc;
    exports aoc.input;
    exports aoc.report;
    exports aoc.util;
    exports aoc.vis;
}