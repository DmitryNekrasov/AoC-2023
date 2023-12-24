import java.io.File
import java.math.BigInteger

data class Point(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
}

data class Line(val p: Point, val q: Point) {
    val a = p.y - q.y
    val b = q.x - p.x
    val c = -a * p.x - b * p.y

    override fun toString(): String {
        return "a = $a, b = $b, c = $c"
    }
}

fun parseLine(line: String): Line {
    fun parsePoint(pointString: String) =
        pointString.split(", ").map { it.toLong() }.let { (x, y, z) -> Point(x, y, z) }
    val (p1, velocity) = line.split(" @ ").map { parsePoint(it) }
    return Line(p1, p1 + velocity)
}

fun main() {
    val points = File("input.txt").useLines { it.toList() }.map { parseLine(it) }
    points.joinToString("\n") { it.toString() }
        .also { println(it) }
}