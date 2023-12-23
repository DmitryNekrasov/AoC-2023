import java.io.File
import kotlin.math.max

fun crossroads(maze: List<String>): Pair<Int, Int> {
    val n = maze.size
    val m = maze.first().length
    var direct = 0
    var reverse = 0
    for (i in 1..<n - 1) {
        for (j in 1..<m - 1) {
            if (maze[i][j] == '.') {
                if (maze[i][j + 1] == '>' && maze[i + 1][j] == 'v') direct++
                if (maze[i][j - 1] == '>' && maze[i - 1][j] == 'v') reverse++
            }
        }
    }
    return direct to reverse
}


fun solvePartOne(mazeStrings: List<String>): Int {
    data class Point(val i: Int, val j: Int)

    val finish = Point(mazeStrings.lastIndex, mazeStrings.first().lastIndex - 1)

    fun backtrack(prevIn: Point, currentIn: Point, mazeIn: List<CharArray>): Int {
        var prev = prevIn
        var current = currentIn
        val maze = mazeIn.map { it.clone() }

        fun Point.next(): List<Point> {
            return listOf(Point(i + 1, j), Point(i - 1, j), Point(i, j + 1), Point(i, j - 1))
                .filter { it != prev }
                .filter { (i, j) ->
                    maze[i][j] == '.' ||
                            current.j < j && maze[i][j] == '>' ||
                            current.i < i && maze[i][j] == 'v'
                }
        }

        var distance = 1
        while (current != finish) {
            maze[current.i][current.j] = '#'
            val next = current.next()
            if (next.isEmpty()) return 0
            if (next.size > 1) return distance + max(
                backtrack(current, next.first(), maze),
                backtrack(current, next.last(), maze)
            )
            distance++
            prev = current
            current = next.first()
        }
        return distance
    }

    return backtrack(Point(0, 1), Point(1, 1), mazeStrings.map { it.toCharArray() })
}

fun main() {
    val maze = File("input.txt").useLines { it.toList() }
    val (direct, reverse) = crossroads(maze)
    println("direct = $direct, reverse = $reverse")
    println(solvePartOne(maze))
}