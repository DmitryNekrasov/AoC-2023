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

fun sort(a: Int, b: Int) = if (a < b) a to b else b to a

fun solve(image: List<String>, k: Int): Long {
    val emptyRows = image.getEmptyRows()
    val emptyCols = image.getEmptyCols()
    val galaxyList = image.toGalaxyList()
    var totalDistance: Long = 0
    for (from in galaxyList) {
        for (to in galaxyList) {
            var distance = from.distance(to)
            val (fromI, toI) = sort(from.i, to.i)
            for (i in fromI + 1..<toI) {
                if (i in emptyRows) {
                    distance += k
                }
            }
            val (fromJ, toJ) = sort(from.j, to.j)
            for (j in fromJ + 1..<toJ) {
                if (j in emptyCols) {
                    distance += k
                }
            }
            totalDistance += distance
        }
    }
    return totalDistance / 2
}

fun main() {
    val image = File("input.txt").useLines { it.toList() }
    val ansPartOne = solve(image, 1)
    println(ansPartOne)
    val ansPartTwo = solve(image, 999_999)
    println(ansPartTwo)
}