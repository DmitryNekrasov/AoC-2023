import java.io.File

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

fun main() {
    val maze = File("input_simple_1.txt").useLines { it.toList() }
    val (direct, reverse) = crossroads(maze)
    println("direct = $direct, reverse = $reverse")
    maze.joinToString("\n")
        .also { println(it) }
}