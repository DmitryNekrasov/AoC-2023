import java.io.File

data class Edge(val direction: Char, val length: Int)

fun solvePartOne(edges: List<Edge>): Int {
    var i = 0
    var area = 1
    for (edge in edges) {
        when (edge.direction) {
            'R' -> {
                area += edge.length
                i += edge.length
            }
            'L' -> i -= edge.length
            'D' -> area += (i + 1) * edge.length
            'U' -> area -= i * edge.length
            else -> throw RuntimeException()
        }
    }
    return area
}

fun main() {
    val input = File("input.txt").useLines { it.toList() }.map { it.split(" ") }
    val edges = input.map { it.let { (d, l, _) -> Edge(d.first(), l.toInt()) } }
    println(solvePartOne(edges))
}