import java.io.File
import java.util.LinkedList
import java.util.Queue

fun solvePartOne(fieldIn: List<CharArray>): Int {
    val field = fieldIn.map { it.copyOf() }
    fun getStart(field: List<CharArray>) =
        field.withIndex().filter { (_, line) -> 'S' in line }.map { (i, line) -> i to line.indexOf('S') }.first()
    val (startRow, startCol) = getStart(field)
    bfs(field, startRow, startCol, 64)
    return field.sumOf { it.count { c -> c == 'E' } }
}

fun bfs(field: List<CharArray>, startRow: Int, startCol: Int, limit: Int) {
    fun opposite(c: Char) = if (c == 'O') 'E' else 'O'
    val n = field.size
    val m = field.first().size
    val queue: Queue<Triple<Int, Int, Int>> = LinkedList()
    queue.offer(Triple(startRow, startCol, 0))
    field[startRow][startCol] = 'E'
    while (queue.isNotEmpty()) {
        val (i, j, stepCount) = queue.poll()
        for ((di, dj) in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)) {
            val nextI = i + di
            val nextJ = j + dj
            if (nextI in 0..<n && nextJ in 0..<m && field[nextI][nextJ] != '#' &&
                (field[nextI][nextJ] == '.' || field[nextI][nextJ] == 'S') && stepCount < limit
            ) {
                field[nextI][nextJ] = opposite(field[i][j])
                queue.offer(Triple(nextI, nextJ, stepCount + 1))
            }
        }
    }
}

fun fullCount(n: Int): Pair<Long, Long> {
    val a = mutableListOf(1L, 0L)
    var x = 0L
    for (i in 0..<n) {
        a[i % 2] = a[i % 2] + x
        x += 4
    }
    return a[0] to a[1]
}

fun solvePartTwo(fieldIn: List<CharArray>): Long {
    val n = fieldIn.size
    val limit = 130
    for (i in 0..<3) {
        for (j in 0..<3) {
            val field = fieldIn.map { it.copyOf() }
            bfs(field, i * (n / 2), j * (n / 2), limit)
            println(field.joinToString("\n") { String(it) })
            println()
        }
    }

    val (eNumber, oNumber) = fullCount(4);
    println("eNumber = $eNumber, oNumber = $oNumber")

    return -1L
}

fun main() {
    val field = File("input.txt").useLines { it.toList() }.map { it.toCharArray() }
    println(solvePartOne(field))
    println(solvePartTwo(field))
}