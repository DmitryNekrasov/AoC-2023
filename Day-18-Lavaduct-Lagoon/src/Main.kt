import java.io.File

data class Edge(val direction: Char, val length: Long)

fun solve(edges: List<Edge>): Long {
    var i = 0L
    var area = 1L
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

fun decodeHex(hex: String): Edge {
    val direction = when (hex[hex.lastIndex - 1]) {
        '0' -> 'R'
        '1' -> 'D'
        '2' -> 'L'
        '3' -> 'U'
        else -> throw RuntimeException()
    }
    return Edge(direction, hex.substring(2..hex.lastIndex - 2).toLong(radix = 16))
}

fun main() {
    val input = File("input.txt").useLines { it.toList() }.map { it.split(" ") }
    val edgesPartOne = input.map { it.let { (d, l, _) -> Edge(d.first(), l.toLong()) } }
    val edgesPartTwo = input.map { decodeHex(it.last()) }
    println(solve(edgesPartOne))
    println(solve(edgesPartTwo))
}