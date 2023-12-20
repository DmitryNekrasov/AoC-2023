import java.io.File
import java.util.LinkedList
import java.util.Queue

enum class ModuleType { FlipFlop, Conjunction, Broadcaster, Terminal }

enum class Signal { Low, High, None }

abstract class Module(val name: String) {
    abstract fun process(from: Module, signal: Signal): Signal

    abstract fun reset()

    companion object {
        var lowCounter = 0L
        var highCounter = 0L
    }
}

class FlipFlop(name: String) : Module(name) {
    private var isOn = false

    override fun process(from: Module, signal: Signal): Signal {
        return when (signal) {
            Signal.High -> Signal.None.also { highCounter++ }
            Signal.Low -> {
                isOn = !isOn
                lowCounter++
                if (isOn) Signal.High else Signal.Low
            }

            else -> throw RuntimeException()
        }
    }

    override fun reset() {
        isOn = false
    }
}

class Conjunction(name: String, private val inputNumber: Int) : Module(name) {
    private val highInputs = HashSet<String>()

    override fun process(from: Module, signal: Signal): Signal {
        if (signal == Signal.None) throw RuntimeException()
        if (signal == Signal.High) {
            highCounter++
            highInputs.add(from.name)
        } else {
            lowCounter++
            highInputs.remove(from.name)
        }
        return if (highInputs.size == inputNumber) Signal.Low else Signal.High
    }

    override fun reset() {
        highInputs.clear()
    }
}

class Broadcaster(name: String) : Module(name) {
    override fun process(from: Module, signal: Signal): Signal {
        throw RuntimeException()
    }

    override fun reset() {}
}

class Terminal(name: String) : Module(name) {
    override fun process(from: Module, signal: Signal): Signal {
        if (signal == Signal.None) throw RuntimeException()
        if (signal == Signal.Low) lowCounter++ else highCounter++
        return Signal.None
    }

    override fun reset() {}
}

fun getNameAndType(vertex: String): Pair<String, ModuleType> {
    return when (vertex[0]) {
        '%' -> vertex.drop(1) to ModuleType.FlipFlop
        '&' -> vertex.drop(1) to ModuleType.Conjunction
        else -> vertex to if (vertex == "broadcaster") ModuleType.Broadcaster else ModuleType.Terminal
    }
}

fun parseLine(line: String, vertexType: HashMap<String, ModuleType>): Pair<String, List<String>> {
    val (from, toString) = line.split(" -> ")
    val (fromName, fromType) = getNameAndType(from)
    vertexType[fromName] = fromType
    val toList = toString.split(", ")
    for (vertex in toList) {
        if (vertex !in vertexType) {
            vertexType[vertex] = ModuleType.Terminal
        }
    }
    return fromName to toList
}

fun numberOfInputsPerConjunctionModule(conjunctionModuleName: String, graph: Map<String, List<String>>): Int {
    return graph.values.count { conjunctionModuleName in it }
}

fun generateNameToModule(
    nameToType: Map<String, ModuleType>,
    conjunctionInputs: Map<String, Int>
): Map<String, Module> {
    return nameToType.map { (name, type) ->
        name to when (type) {
            ModuleType.FlipFlop -> FlipFlop(name)
            ModuleType.Conjunction -> Conjunction(name, conjunctionInputs[name]!!)
            ModuleType.Broadcaster -> Broadcaster(name)
            ModuleType.Terminal -> Terminal(name)
        }
    }.associateBy({ it.first }, { it.second })
}

fun pushButton(graph: Map<String, List<String>>, nameToModule: Map<String, Module>) {
    Module.lowCounter++
    val queue: Queue<Pair<Module, Signal>> = LinkedList()
    queue.offer(nameToModule["broadcaster"]!! to Signal.Low)
    while (queue.isNotEmpty()) {
        val (from, signal) = queue.poll()
        for (toName in graph[from.name]!!) {
            val to = nameToModule[toName]!!
            val nextSignal = to.process(from, signal)
            if (nextSignal != Signal.None) {
                queue.offer(to to nextSignal)
            }
        }
    }
}

fun solvePartOne(times: Int, graph: Map<String, List<String>>, nameToModule: Map<String, Module>): Long {
    repeat(times) { pushButton(graph, nameToModule) }
    return Module.lowCounter * Module.highCounter
}

fun reset(nameToModule: Map<String, Module>) {
    Module.lowCounter = 0
    Module.highCounter = 0
    nameToModule.values.forEach { module -> module.reset() }
}

fun solvePartTwo(graph: Map<String, List<String>>, nameToModule: Map<String, Module>): Long {
    reset(nameToModule)

    return -1L
}

fun main() {
    val nameToType = HashMap<String, ModuleType>()
    val graph = File("input.txt").useLines { it.toList() }.map { parseLine(it, nameToType) }
        .associateBy({ it.first }, { it.second })
    val conjunctionInputs = nameToType.filter { it.value == ModuleType.Conjunction }
        .map { it.key to numberOfInputsPerConjunctionModule(it.key, graph) }.associateBy({ it.first }, { it.second })
    val nameToModule = generateNameToModule(nameToType, conjunctionInputs)

    println(solvePartOne(1000, graph, nameToModule))
    println(solvePartTwo(graph, nameToModule))
}