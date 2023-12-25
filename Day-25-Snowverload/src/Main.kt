import java.io.File

typealias Edges = List<Pair<String, String>>
typealias Graph = HashMap<String, MutableList<String>>

fun parseLine(line: String): Edges {
    val (from, toList) = line.split(": ")
    return toList.split(" ").map { from to it }
}

fun Edges.toGraph(): Graph {
    val result = Graph()
    for ((from, to) in this) {
        result[from] = (result[from] ?: mutableListOf()).also { it.add(to) }
        result[to] = (result[to] ?: mutableListOf()).also { it.add(from) }
    }
    return result
}

fun solve(edges: Edges, graph: Graph): Int {
    println(edges)
    graph.toList().joinToString("\n") { it.toString() }
        .also { println(it) }

    return -1
}

fun main() {
    val edges = File("input_simple_1.txt").useLines { it.toList() }.map { parseLine(it) }.flatten()
    val graph = edges.toGraph()
    println(solve(edges, graph))
}