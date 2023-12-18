import java.io.File

data class Edge(val direction: Char, val length: Int, val color: String)

fun solvePartOne(edges: List<Edge>): Int {
    println(edges)

    return -1
}

fun main() {
    val edges = File("input_simple_1.txt").useLines { it.toList() }
        .map { it.split(" ").let { (d, l, c) -> Edge(d.first(), l.toInt(), c) } }
    println(solvePartOne(edges))
}