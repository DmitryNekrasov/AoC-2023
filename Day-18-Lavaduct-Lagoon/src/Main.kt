import java.io.File
import kotlin.math.*

data class Edge(val direction: Char, val length: Int, val color: String)

fun calcSizeAndStart(edges: List<Edge>): IntArray {
    var minI = Int.MAX_VALUE
    var minJ = Int.MAX_VALUE
    var maxI = 0
    var maxJ = 0
    var i = 0
    var j = 0
    for (edge in edges) {
        when (edge.direction) {
            'R' -> j += edge.length
            'L' -> j -= edge.length
            'D' -> i += edge.length
            'U' -> i -= edge.length
            else -> throw RuntimeException()
        }
        minI = min(minI, i)
        minJ = min(minJ, j)
        maxI = max(maxI, i)
        maxJ = max(maxJ, j)
    }
    return intArrayOf(maxI - minI + 1, maxJ - minJ + 1, -minI, -minJ)
}

fun solvePartOne(edges: List<Edge>): Int {
    println(edges)
    val (n, m, startI, startJ) = calcSizeAndStart(edges)
    println("n = $n, m = $m, startI = $startI, startJ = $startJ")

    return -1
}

fun main() {
    val edges = File("input_simple_1.txt").useLines { it.toList() }
        .map { it.split(" ").let { (d, l, c) -> Edge(d.first(), l.toInt(), c) } }
    println(solvePartOne(edges))
}