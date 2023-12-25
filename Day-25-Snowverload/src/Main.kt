import java.io.File
import kotlin.random.Random

typealias Edges = List<Pair<String, String>>
typealias MutableEdges = MutableList<Pair<String, String>>
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

fun Graph.copy(): Graph {
    val result = Graph()
    for ((from, to) in this) {
        result[from] = to.toMutableList()
    }
    return result
}

fun Graph.removeVertex(v: String) {
    for (to in this.values) {
        if (v in to)
            to.removeIf { it == v }
    }
}

fun karger(edges: MutableEdges, graph: Graph): Triple<Int, Int, Int> {
    val weights = HashMap<String, Int>()
    for ((from, to) in edges) {
        weights[from] = 1
        weights[to] = 1
    }
    val n = graph.size
    repeat(n - 2) {
        val randomIndex = Random.nextInt(edges.size)
        val (u, v) = edges[randomIndex]
        edges.removeIf { it.first == v || it.second == v }
        graph.removeVertex(v)
        weights[u] = weights[u]!! + weights[v]!!
        weights.remove(v)
        for (x in graph[v]!!.toList()) {
            if (u != x && x != v) {
                graph[u]!!.add(x)
                graph[x]!!.add(u)
                edges.add(u to x)
                edges.add(x to u)
            }
        }
        graph.remove(v)
    }
    return Triple(edges.size, weights.values.first(), weights.values.last())
}

fun solve(edges: Edges, graph: Graph): Int {
    var iteration = 1
    do {
        println("Iteration: $iteration")
        iteration++
        val graphCopy = graph.copy()
        val (size, c1, c2) = karger(edges.toMutableList(), graphCopy)
        if (size == 6) return c1 * c2
    } while (true)
}

fun main() {
    val edges = File("input.txt").useLines { it.toList() }.map { parseLine(it) }.flatten()
    val graph = edges.toGraph()
    println(solve(edges, graph))
}