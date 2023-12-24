import java.io.File
import java.math.BigDecimal
import java.math.MathContext

data class Point(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)

    fun inLimit() = x in BOTTOM_LIMIT..UPPER_LIMIT && y in BOTTOM_LIMIT..UPPER_LIMIT

    companion object {
        val BOTTOM_LIMIT = BigDecimal("200000000000000")
        val UPPER_LIMIT = BigDecimal("400000000000000")
    }
}

fun distance(p: Point, q: Point): BigDecimal =
    ((p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y)).sqrt(MathContext(10))

fun det(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal) = a * d - b * c

data class Direction(val x: Int, val y: Int)

fun direction(p: Point, q: Point) = Direction((p.x - q.x).signum(), (p.y - q.y).signum())

class Line(private val p: Point, velocityPoint: Point) {
    private val a: BigDecimal
    private val b: BigDecimal
    private val c: BigDecimal
    private val direction: Direction
    private val velocity: BigDecimal

    init {
        val q = p + velocityPoint
        a = p.y - q.y
        b = q.x - p.x
        c = -a * p.x - b * p.y
        direction = direction(p, q)
        velocity = distance(p, velocityPoint)
    }

    private infix fun parallel(other: Line) = det(a, b, other.a, other.b).abs() < EPS

    private fun intersect(other: Line): Point {
        val zn = det(a, b, other.a, other.b)
        return Point(-det(c, b, other.c, other.b) / zn, -det(a, c, other.a, other.c) / zn, BigDecimal.ZERO)
    }

    infix fun collide(other: Line): Boolean {
        if (this parallel other) return false
        val intersection = intersect(other)
        return intersection.inLimit() &&
                direction == direction(p, intersection) && other.direction == direction(other.p, intersection)
    }

    override fun toString(): String {
        return "p = $p, a = $a, b = $b, c = $c"
    }

    companion object {
        val EPS = BigDecimal("0.000000000001")
    }
}

fun solve(lines: List<Line>): Int {
    var result = 0
    for (i in lines.indices) {
        val line1 = lines[i]
        for (j in i + 1..lines.lastIndex) {
            val line2 = lines[j]
            if (line1 collide line2) {
                result++
            }
        }
    }
    return result
}

fun parseLine(line: String): Line {
    fun parsePoint(pointString: String) =
        pointString.split(", ").map { BigDecimal.valueOf(it.trim().toLong()) }.let { (x, y, z) -> Point(x, y, z) }
    return line.split(" @ ").map { parsePoint(it) }.let { (p1, velocity) -> Line(p1, velocity) }
}

fun main() {
    val lines = File("input.txt").useLines { it.toList() }.map { parseLine(it) }
    println(solve(lines))
}