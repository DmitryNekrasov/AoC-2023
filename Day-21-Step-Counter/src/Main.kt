import java.io.File
import java.util.LinkedList
import java.util.Queue

const val PART_TWO_STEP_NUMBER = 26_501_365

fun solvePartOne(fieldIn: List<CharArray>): Int {
    val field = fieldIn.map { it.copyOf() }
    fun getStart(field: List<CharArray>) =
        field.withIndex().filter { (_, line) -> 'S' in line }.map { (i, line) -> i to line.indexOf('S') }.first()
    val (startRow, startCol) = getStart(field)
    bfs(field, startRow, startCol, 64)
    return field.sumOf { it.count { c -> c == 'E' } }
}

fun bfs(field: List<CharArray>, startRow: Int, startCol: Int, limit: Int, initial: Char = 'E') {
    fun opposite(c: Char) = if (c == 'O') 'E' else 'O'
    val n = field.size
    val m = field.first().size
    val queue: Queue<Triple<Int, Int, Int>> = LinkedList()
    queue.offer(Triple(startRow, startCol, 0))
    field[startRow][startCol] = initial
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

fun full(fieldIn: List<CharArray>): Pair<Int, Int> { // O count, E count
    val field = fieldIn.map { it.copyOf() }
    val n = fieldIn.size
    bfs(field, n / 2, n / 2, n)
    return field.sumOf { it.count { c -> c == 'O' } } to field.sumOf { it.count { c -> c == 'E' } }
}

fun eNSEW(fieldIn: List<CharArray>): List<Int> {
    val n = fieldIn.size
    val result = mutableListOf<Int>()
    for ((i, j) in listOf(0 to n / 2, n - 1 to n / 2, n / 2 to 0, n / 2 to n - 1)) {
        val field = fieldIn.map { it.copyOf() }
        bfs(field, i, j, n - 1, 'O')
        result.add(field.sumOf { it.count { c -> c == 'O' } })
    }
    return result
}

fun oNWNESESW(fieldIn: List<CharArray>): List<Int> {
    val n = fieldIn.size
    val result = mutableListOf<Int>()
    for ((i, j) in listOf(0 to 0, 0 to n - 1, n - 1 to n - 1, n - 1 to 0)) {
        val field = fieldIn.map { it.copyOf() }
        bfs(field, i, j, n / 2, 'O')
        result.add(field.sumOf { it.count { c -> c == 'O' } })
    }
    return result
}

fun eNWNESESW(fieldIn: List<CharArray>): List<Int> {
    val n = fieldIn.size
    val result = mutableListOf<Int>()
    for ((i, j) in listOf(0 to 0, 0 to n - 1, n - 1 to n - 1, n - 1 to 0)) {
        val field = fieldIn.map { it.copyOf() }
        bfs(field, i, j, n - 1 + n / 2, 'E')
        result.add(field.sumOf { it.count { c -> c == 'O' } })
    }
    return result
}

fun fullCount(n: Int): Pair<Long, Long> { // O count, E count
    val a = mutableListOf(1L, 0L)
    var x = 0L
    for (i in 0..<n - 1) {
        a[i % 2] = a[i % 2] + x
        x += 4
    }
    return a[0] to a[1]
}

fun solvePartTwo(fieldIn: List<CharArray>): Long {
    val size = fieldIn.size
    val n = (PART_TWO_STEP_NUMBER - size / 2) / size + 1
    val (oFull, eFull) = full(fieldIn)
    val (oNumber, eNumber) = fullCount(n)
    val eNSEW = eNSEW(fieldIn)
    val eNWNESESW = eNWNESESW(fieldIn)
    val oNWNESESW = oNWNESESW(fieldIn)
    return eNSEW.sum() + (n - 1).toLong() * oNWNESESW.sum() + (n - 2).toLong() * eNWNESESW.sum() + oNumber * oFull + eNumber * eFull
}

fun main() {
    val field = File("input.txt").useLines { it.toList() }.map { it.toCharArray() }
    println(solvePartOne(field))
    println(solvePartTwo(field))
}