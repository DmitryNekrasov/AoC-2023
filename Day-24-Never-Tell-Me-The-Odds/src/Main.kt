import java.io.File

data class Point(val x: Long, val y: Long, val z: Long)

fun parseLine(line: String): Pair<Point, Point> {
    fun parsePoint(pointString: String) =
        pointString.split(", ").map { it.toLong() }.let { (x, y, z) -> Point(x, y, z) }
    return line.split(" @ ").map { parsePoint(it) }.let { (p1, p2) -> p1 to p2 }
}

fun main() {
    val points = File("input.txt").useLines { it.toList() }.map { parseLine(it) }
    points.joinToString("\n") { it.toString() }
        .also { println(it) }
}