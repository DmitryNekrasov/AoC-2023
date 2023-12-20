import java.io.File

enum class ModuleType { FlipFlop, Conjunction, Broadcaster, Terminal }

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

fun main() {
    val vertexType = HashMap<String, ModuleType>()
    val graph = File("input.txt").useLines { it.toList() }.map { parseLine(it, vertexType) }
        .associateBy({ it.first }, { it.second })
    val conjunctionInputs = vertexType.filter { it.value == ModuleType.Conjunction }
        .map { it.key to numberOfInputsPerConjunctionModule(it.key, graph) }.associateBy({ it.first }, { it.second })

    println(graph.toList().joinToString("\n") { it.toString() })
    println(vertexType.toList().joinToString("\n") { it.toString() })
    println(conjunctionInputs)
}