import java.io.File
import kotlin.math.max

data class Point(val i: Int, val j: Int)

fun Char.isFree() = this != '#'

fun Boolean.toInt() = if (this) 1 else 0

fun crossroads(maze: List<String>): Map<Point, Int> {
    val n = maze.size
    val m = maze.first().length
    val result = mutableListOf(Point(0, 1))
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
    result.add(Point(n - 1, m - 2))
    return result.withIndex().associateBy({ it.value }, { it.index })
}

fun compress(maze: List<String>, isPartTwo: Boolean): Array<MutableList<Pair<Int, Int>>> {
    val crossroads = crossroads(maze)

    val n = maze.size
    val m = maze.first().length

    fun Point.isValid() = i in 0..<n && j in 0..<m && maze[i][j].isFree()

    fun Point.next(prev: Point): Point {
        return listOf(Point(i + 1, j), Point(i - 1, j), Point(i, j + 1), Point(i, j - 1))
            .filter { it.isValid() }.first { it != prev }
    }

    fun edge(last: Point, point: Point): Triple<Int, Int, Int> {
        var prev = last
        var current = point
        var distance = 1
        while (current !in crossroads) {
            distance++
            val next = current.next(prev)
            prev = current
            current = next
        }
        return Triple(crossroads[last]!!, crossroads[current]!!, distance)
    }

    fun edges(point: Point): List<Triple<Int, Int, Int>> { // from, to, distance
        val (i, j) = point
        val candidates = mutableListOf(Point(i, j + 1), Point(i + 1, j))
        if (isPartTwo) {
            candidates.add(Point(i, j - 1))
            candidates.add(Point(i - 1, j))
        }
        return candidates.filter { it.isValid() }.map { edge(point, it) }
    }

    val crossroadsNumber = crossroads.size
    val graph = Array(crossroadsNumber) { mutableListOf<Pair<Int, Int>>() }
    for ((point, _) in crossroads) {
        for ((from, to, distance) in edges(point)) {
            graph[from].add(to to distance)
        }
    }

    return graph
}

fun solve(maze: List<String>, isPartTwo: Boolean = false): Int {
    val graph = compress(maze, isPartTwo)
    val n = graph.size
    val visited = BooleanArray(n) { false }
    var result = 0
    fun backtrack(from: Int, total: Int) {
        if (from == n - 1) {
            result = max(result, total)
        } else {
            visited[from] = true
            for ((to, distance) in graph[from]) {
                if (!visited[to]) {
                    backtrack(to, total + distance)
                }
            }
            visited[from] = false
        }
    }
    backtrack(0, 0)
    return result
}

fun main() {
    val maze = File("input.txt").useLines { it.toList() }
    println(solve(maze))
    println(solve(maze, isPartTwo = true))
}