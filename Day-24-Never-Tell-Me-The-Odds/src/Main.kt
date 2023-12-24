import java.io.File
import java.math.BigDecimal

val BOTTOM_LIMIT = BigDecimal("7")
val UPPER_LIMIT = BigDecimal("27")

data class Point(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)

    fun inLimit() = x in BOTTOM_LIMIT..UPPER_LIMIT && y in BOTTOM_LIMIT..UPPER_LIMIT
}

fun det(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal) = a * d - b * c

class Line(val p: Point, val q: Point) {
    val a = p.y - q.y
    val b = q.x - p.x
    val c = -a * p.x - b * p.y

    infix fun parallel(other: Line) = (det(a, b, other.a, other.b).abs() < EPS).also { if (it) println("PARALLEL") }

    infix fun equivalent(other: Line) = (det(a, b, other.a, other.b).abs() < EPS &&
            det(a, c, other.a, other.c).abs() < EPS &&
            det(b, c, other.b, other.c).abs() < EPS).also { if (it) println("EQUIVALENT") }

    infix fun intersect(other: Line): Point {
        val zn = det(a, b, other.a, other.b)
        return Point(-det(c, b, other.c, other.b) / zn, -det(a, c, other.a, other.c) / zn, BigDecimal.ZERO)
    }

    override fun toString(): String {
        return "p = $p, q = $q, a = $a, b = $b, c = $c"
    }

    companion object {
        val EPS = BigDecimal("0.00000000001")
    }
}

fun solve(lines: List<Line>): Int {
    lines.joinToString("\n") { it.toString() }
        .also { println(it) }
    println("Line number = ${lines.size}")
    var result = 0
    for (i in lines.indices) {
        val line1 = lines[i]
        for (j in i + 1..lines.lastIndex) {
            val line2 = lines[j]
            if (!(line1 parallel line2) && (line1 equivalent line2 || (line1 intersect line2).inLimit())) {
                println(line1)
                println(line2)
                println("--------------------------------------------------------------------------------")
                result++
            }
        }
    }
    return result
}

fun parseLine(line: String): Line {
    fun parsePoint(pointString: String) =
        pointString.split(", ").map { BigDecimal.valueOf(it.trim().toLong()) }.let { (x, y, z) -> Point(x, y, z) }
    val (p1, velocity) = line.split(" @ ").map { parsePoint(it) }
    return Line(p1, p1 + velocity)
}

fun main() {
    val lines = File("input_simple_1.txt").useLines { it.toList() }.map { parseLine(it) }
    println(solve(lines))
}