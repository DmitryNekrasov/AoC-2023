import java.io.File
import java.math.BigDecimal

data class Point(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
}

fun det(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal) = a * d - b * c

class Line(p: Point, q: Point) {
    val a = p.y - q.y
    val b = q.x - p.x
    val c = -a * p.x - b * p.y

    infix fun parallel(other: Line) = det(a, b, other.a, other.b).abs() < EPS

    infix fun equivalent(other: Line) = det(a, b, other.a, other.b).abs() < EPS &&
            det(a, c, other.a, other.c).abs() < EPS &&
            det(b, c, other.b, other.c).abs() < EPS

    override fun toString(): String {
        return "a = $a, b = $b, c = $c"
    }

    companion object {
        val EPS = BigDecimal("0.00000000001")
    }
}

fun parseLine(line: String): Line {
    fun parsePoint(pointString: String) =
        pointString.split(", ").map { BigDecimal.valueOf(it.toLong()) }.let { (x, y, z) -> Point(x, y, z) }
    val (p1, velocity) = line.split(" @ ").map { parsePoint(it) }
    return Line(p1, p1 + velocity)
}

fun main() {
    val points = File("input.txt").useLines { it.toList() }.map { parseLine(it) }
    points.joinToString("\n") { it.toString() }
        .also { println(it) }
}