import java.io.File
import kotlin.math.max

data class Point(val x: Int, val y: Int, val z: Int)

data class Brick(val p1: Point, val p2: Point) {
    val over = HashSet<Brick>()
    val under = HashSet<Brick>()

    infix fun over(brick: Brick) = over.add(brick)
    infix fun under(brick: Brick) = under.add(brick)
}

fun parseBrick(brickString: String): Brick {
    fun parsePoint(pointString: String): Point =
        pointString.split(",").map { it.toInt() }.let { (x, y, z) -> Point(x, y, z) }

    fun Boolean.toInt() = if (this) 1 else 0

    return brickString.split("~")
        .let { (p1String, p2String) ->
            Brick(parsePoint(p1String), parsePoint(p2String))
                .also { (p1, p2) ->
                    if (p1.x > p2.x ||
                        p1.y > p2.y ||
                        p1.z > p2.z
                    ) throw RuntimeException()
                }
                .also { (p1, p2) ->
                    if ((p1.x == p2.x).toInt() +
                        (p1.y == p2.y).toInt() +
                        (p1.z == p2.z).toInt() < 2
                    ) throw RuntimeException()
                }
        }
}

fun placeBricksOnTopOfEachOther(bricks: List<Brick>) {
    val (n, m) = bricks.fold(0 to 0) { acc, b ->
        max(acc.first, max(b.p1.x, b.p2.x)) to
                max(acc.second, max(b.p1.y, b.p2.y))
    }.let { it.first + 1 to it.second + 1 }
    val land = Brick(Point(0, 0, 0), Point(n - 1, m - 1, 0))
    val a = Array(n) { Array(m) { 0 to land } }
    for (brick in bricks) {
        val (p1, p2) = brick
        if (p1.z != p2.z) {
            val (x, y) = p1.x to p1.y
            val len = p2.z - p1.z + 1
            val (lastHeight, lastBrick) = a[x][y]
            a[x][y] = lastHeight + len to brick
            brick over lastBrick
            lastBrick under brick
        } else {
            var maxCurrentHeight = 0
            for (i in p1.x..p2.x) {
                for (j in p1.y..p2.y) {
                    if (a[i][j].first > maxCurrentHeight) {
                        maxCurrentHeight = a[i][j].first
                    }
                }
            }
            for (i in p1.x..p2.x) {
                for (j in p1.y..p2.y) {
                    val (lastHeight, lastBrick) = a[i][j]
                    a[i][j] = maxCurrentHeight + 1 to brick
                    if (lastHeight == maxCurrentHeight) {
                        brick over lastBrick
                        lastBrick under brick
                    }
                }
            }
        }
    }
}

fun solvePartOne(bricks: List<Brick>): Int {
    return bricks.count { brick -> brick.under.all { it.over.size > 1 } }
}

fun main() {
    val bricks = File("input.txt")
        .useLines { it.toList() }
        .map { parseBrick(it) }
        .sortedBy { it.p1.z }
        .also { placeBricksOnTopOfEachOther(it) }
    println(solvePartOne(bricks))
}