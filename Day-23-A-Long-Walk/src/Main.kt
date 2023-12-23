import java.io.File

data class Point(val i: Int, val j: Int)

fun Char.isFree() = this != '#'
fun Boolean.toInt() = if (this) 1 else 0

fun crossroads(maze: List<String>): List<Point> {
    val n = maze.size
    val m = maze.first().length
    val result = mutableListOf(Point(0, 1), Point(n - 1, m - 2))
    for (i in 1..<n - 1) {
        for (j in 1..<m - 1) {
            if (maze[i][j] == '.') {
                if (listOf(maze[i - 1][j], maze[i + 1][j], maze[i][j - 1], maze[i][j + 1])
                        .sumOf { it.isFree().toInt() } >= 3
                ) {
                    result.add(Point(i, j))
                }
            }
        }
    }
    return result
}

fun compress(maze: List<String>) {
    val crossroads = crossroads(maze)
    println("crossroads number = ${crossroads.size}")
    println(crossroads)
}

fun solvePartOne(maze: List<String>): Int {
    compress(maze)
    return -1
}

fun main() {
    val maze = File("input.txt").useLines { it.toList() }
    println(solvePartOne(maze))
}