import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

enum class RelayType {
    Broadcaster,
    FlipFlop,
    Conjunction
}

enum class Pulse { LOW, HIGH }

sealed class Module(val id: String, val outputs: MutableList<Module> = mutableListOf()) {
    abstract fun reset()
    /** Returns the pulse to send to the next module, or null if no pulse is sent. */
    abstract fun receive(source: String, pulse: Pulse): Pulse?
}

object Button : Module("button") {
    override fun reset() {}
    override fun receive(source: String, pulse: Pulse) = null
}

object Rx : Module("rx") {
    var on = false
    override fun reset() { on = false }
    override fun receive(source: String, pulse: Pulse): Pulse? {
        on = pulse == Pulse.LOW
        return null
    }
}

class Broadcaster(id: String) : Module(id) {
    override fun reset() {}
    override fun receive(source: String, pulse: Pulse) = pulse
}

class FlipFlop(id: String) : Module(id) {
    var on: Boolean = false
    override fun reset() { on = false }
    override fun receive(source: String, pulse: Pulse): Pulse? {
        return when (pulse) {
            Pulse.LOW -> {
                on = !on
                if (on) Pulse.HIGH else Pulse.LOW
            }
            Pulse.HIGH -> null
        }
    }
}

class Conjunction(id: String) : Module(id) {
    val lastInputs = mutableMapOf<String, Pulse>()
    fun signal() = if (lastInputs.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
    override fun reset() {
        lastInputs.keys.forEach { lastInputs[it] = Pulse.LOW }
    }
    override fun receive(source: String, pulse: Pulse): Pulse {
        lastInputs[source] = pulse
        return signal()
    }
}

val testInput = """
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> rx
""".parselines

fun List<String>.parse(): Map<String, Module> {
    val modules = mutableMapOf<String, Module>("rx" to Rx)
    val outputs = mutableMapOf<String, List<String>>("rx" to listOf())
    forEach { line ->
        val type = line.chunk(0)
        val module = when {
            type.startsWith("&") -> Conjunction(type.drop(1))
            type.startsWith("%") -> FlipFlop(type.drop(1))
            else -> Broadcaster(type)
        }
        modules[module.id] = module
        outputs[module.id] = line.substringAfter("->").trim().split(",").map { it.trim() }
    }
    modules.forEach {
        it.value.outputs.addAll(outputs[it.key]!!.map { id -> modules[id]!! })
    }
    modules.values.filterIsInstance<Conjunction>().forEach { mod ->
        outputs.keys.filter { mod.id in outputs[it]!! }.forEach {
            mod.lastInputs[it] = Pulse.LOW
        }
    }
    return modules
}

fun Map<String, Module>.reset() {
    values.forEach { it.reset() }
}

/** Low and high pulses sent. */
typealias PulseCount = Pair<Int, Int>

fun Map<String, Module>.pushButton(lowPulse: (String) -> Unit = { }): PulseCount {
    var pulseCount = 0 to 0
    val activeSignals = mutableListOf(Triple(Button as Module, get("broadcaster")!!, Pulse.LOW))
    while (activeSignals.isNotEmpty()) {
        val (source, dest, pulse) = activeSignals.removeAt(0)
        pulseCount = when (pulse) {
            Pulse.LOW -> pulseCount.copy(first = pulseCount.first + 1)
            Pulse.HIGH -> pulseCount.copy(second = pulseCount.second + 1)
        }
        val nextPulse = dest.receive(source.id, pulse)
        if (nextPulse != null) {
            if (nextPulse == Pulse.LOW)
                lowPulse(dest.id)
            activeSignals.addAll(dest.outputs.map { Triple(dest, it, nextPulse) })
        }
    }
    return pulseCount
}

// part 1

fun List<String>.part1(): Long {
    val mods = parse()
    mods.reset()
    val result = (1..1000).map { mods.pushButton() }
    val low = result.sumOf { it.first }.toLong()
    val high = result.sumOf { it.second }.toLong()
    return low * high
}

// part 2

fun List<String>.part2(): Long {
    val checkProgress = true
    val mods = parse()
    mods.reset()
    var presses = 0
    val signals = listOf("qq", "fj", "jc", "vm").map { mods[it] as Conjunction }.associateWith { mutableListOf<Pulse>() }
    repeat(10000) {
        mods.pushButton { lowId ->
            if (lowId in listOf("qq","fj","jc","vm","rx"))
                println("Sending low pulse from $lowId during press #${presses+1}")
        }
        presses++
        if (checkProgress) {
            val sl = signals.keys.map { it.signal() }
            if (sl.any { it == Pulse.LOW }) {
                println("After $presses, key signals are $sl")
            }
        }
        if (Rx.on) return presses.toLong()
    }
    return 3803L*3877*3889*3917
}

// calculate answers

val day = 20
val input = getDayInput(day, 2023)
val testResult = testInput.part1()
val testResult2 = 0
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
