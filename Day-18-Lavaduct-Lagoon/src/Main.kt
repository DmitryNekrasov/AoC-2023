import java.io.File
import java.util.LinkedList
import java.util.Queue
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
    return intArrayOf(maxI - minI + 3, maxJ - minJ + 3, 1 - minI, 1 - minJ)
}

fun fill(field: Array<CharArray>) {
    val n = field.size
    val m = field.first().size
    val queue: Queue<Pair<Int, Int>> = LinkedList()
    queue.offer(0 to 0)
    while (queue.isNotEmpty()) {
        val (i, j) = queue.poll()
        if (i < 0 || i >= n || j < 0 || j >= m || field[i][j] != ' ') continue
        field[i][j] = '.'
        for ((di, dj) in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)) {
            queue.offer(i + di to j + dj)
        }
    }
    for (i in 0..<n) {
        for (j in 0..<m) {
            if (field[i][j] != '.') {
                field[i][j] = '#'
            }
        }
    }
}

fun getField(edges: List<Edge>, n: Int, m: Int, startI: Int, startJ: Int): Array<CharArray> {
    val result = Array(n) { CharArray(m) { ' ' } }
    var i = startI
    var j = startJ
    for (edge in edges) {
        when (edge.direction) {
            'R' -> {
                (0..edge.length).forEach { result[i][j + it] = '#' }
                j += edge.length
            }
            'L' -> {
                (0..edge.length).forEach { result[i][j - it] = '#' }
                j -= edge.length
            }
            'D' -> {
                (0..edge.length).forEach { result[i + it][j] = '#' }
                i += edge.length
            }
            'U' -> {
                (0..edge.length).forEach { result[i - it][j] = '#' }
                i -= edge.length
            }
            else -> throw RuntimeException()
        }
    }
    fill(result)
    return result
}

fun solvePartOne(edges: List<Edge>): Int {
    val (n, m, startI, startJ) = calcSizeAndStart(edges)
    val field = getField(edges, n, m, startI, startJ)
    field.joinToString("\n") { String(it) }
        .also { println(it) }

    return -1
}

fun main() {
    val edges = File("input_simple_1.txt").useLines { it.toList() }
        .map { it.split(" ").let { (d, l, c) -> Edge(d.first(), l.toInt(), c) } }
    println(solvePartOne(edges))
}