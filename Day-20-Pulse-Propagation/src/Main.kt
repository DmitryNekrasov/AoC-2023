import java.io.File

enum class ModuleType { FlipFlop, Conjunction, Broadcaster, Terminal }

enum class Signal { Low, High, None }

abstract class Module(val name: String) {
    abstract fun process(from: Module, signal: Signal): Signal
}

class FlipFlop(name: String) : Module(name) {
    private var isOn = false

    override fun process(from: Module, signal: Signal): Signal {
        return when (signal) {
            Signal.High -> Signal.None
            Signal.Low -> {
                isOn = !isOn
                if (isOn) Signal.High else Signal.Low
            }

            else -> throw RuntimeException()
        }
    }
}

class Conjunction(name: String, private val inputNumber: Int) : Module(name) {
    private val highInputs = HashSet<String>()

    override fun process(from: Module, signal: Signal): Signal {
        if (signal == Signal.None) throw RuntimeException()
        if (signal == Signal.High) {
            highInputs.add(from.name)
        } else {
            highInputs.remove(from.name)
        }
        return if (highInputs.size == inputNumber) Signal.Low else Signal.High
    }
}

class Broadcaster(name: String) : Module(name) {
    override fun process(from: Module, signal: Signal): Signal {
        if (signal == Signal.None) throw RuntimeException()
        return signal
    }
}

class Terminal(name: String) : Module(name) {
    override fun process(from: Module, signal: Signal): Signal {
        if (signal == Signal.None) throw RuntimeException()
        return Signal.None
    }
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

fun main() {
    val nameToType = HashMap<String, ModuleType>()
    val graph = File("input.txt").useLines { it.toList() }.map { parseLine(it, nameToType) }
        .associateBy({ it.first }, { it.second })
    val conjunctionInputs = nameToType.filter { it.value == ModuleType.Conjunction }
        .map { it.key to numberOfInputsPerConjunctionModule(it.key, graph) }.associateBy({ it.first }, { it.second })
    val nameToModule = generateNameToModule(nameToType, conjunctionInputs)

    println(graph.toList().joinToString("\n") { it.toString() })
    println(nameToType.toList().joinToString("\n") { it.toString() })
    println(conjunctionInputs)
}