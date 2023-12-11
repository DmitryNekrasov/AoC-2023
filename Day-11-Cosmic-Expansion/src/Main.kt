import java.io.File
import kotlin.math.abs

data class Galaxy(val i: Int, val j: Int) {
    fun distance(o: Galaxy) = abs(i - o.i) + abs(j - o.j)
}

fun List<String>.getEmptyRows() = this.withIndex().filter { (_, row) -> row.all { it == '.' } }.map { it.index }.toSet()

fun List<String>.getEmptyCols(): Set<Int> {
    val result = HashSet<Int>()
    val n = size
    val m = first().length
    for (j in 0..<m) {
        var colIsEmpty = true
        for (i in 0..<n) {
            colIsEmpty = colIsEmpty && this[i][j] == '.'
        }
        if (colIsEmpty) {
            result.add(j)
        }
    }
    return result
}

fun List<String>.toGalaxyList() = this.withIndex()
    .map { (i, row) -> row.withIndex().filter { (_, c) -> c == '#' }.map { Galaxy(i, it.index) } }
    .flatten()

fun solve(image: List<String>): Int {
    val emptyRows = image.getEmptyRows()
    val emptyCols = image.getEmptyCols()
    val galaxyList = image.toGalaxyList()

    println(emptyRows)
    println(emptyCols)
    println(galaxyList)

    return -1
}

fun main() {
    val image = File("input_simple_1.txt").useLines { it.toList() }
    val ansPartOne = solve(image)
    println(ansPartOne)
}