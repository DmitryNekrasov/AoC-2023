import java.io.File
import java.util.LinkedList
import java.util.Queue

const val MAX_STEP_NUMBER = 64

fun getStart(field: List<CharArray>) =
    field.withIndex().filter { (_, line) -> 'S' in line }.map { (i, line) -> i to line.indexOf('S') }.first()

fun solve(field: List<CharArray>): Int {
    fun opposite(c: Char) = if (c == 'O') 'E' else 'O'
    val (startRow, startCol) = getStart(field)
    val n = field.size
    val m = field.first().size
    val queue: Queue<Triple<Int, Int, Int>> = LinkedList()
    queue.offer(Triple(startRow, startRow, 0))
    while (queue.isNotEmpty()) {
        val (i, j, stepCount) = queue.poll()
        for ((di, dj) in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)) {
            val nextI = i + di
            val nextJ = j + dj
            if (nextI in 0..<n && nextJ in 0..<m && field[nextI][nextJ] != '#' && field[nextI][nextJ] == '.' && stepCount < MAX_STEP_NUMBER) {
                field[nextI][nextJ] = opposite(field[i][j])
                queue.offer(Triple(nextI, nextJ, stepCount + 1))
            }
        }
    }
    return 1 + field.sumOf { it.count { it == 'E' } }
}

fun main() {
    val field = File("input.txt").useLines { it.toList() }.map { it.toCharArray() }
    println(solve(field))
}