import java.io.File

data class Point(val x: Int, val y: Int, val z: Int)

data class Brick(val p1: Point, val p2: Point)

fun parseBrick(brickString: String): Brick {
    fun parsePoint(pointString: String): Point =
        pointString.split(",").map { it.toInt() }.let { (x, y, z) -> Point(x, y, z) }

    fun Boolean.toInt() = if (this) 1 else 0

    return brickString.split("~")
        .let { (p1String, p2String) ->
            Brick(parsePoint(p1String), parsePoint(p2String))
                .also { brick -> if (brick.p1.z > brick.p2.z) throw RuntimeException() }
                .also { brick ->
                    if ((brick.p1.x == brick.p2.x).toInt() +
                        (brick.p1.y == brick.p2.y).toInt() +
                        (brick.p1.z == brick.p2.z).toInt() < 2
                    ) throw RuntimeException().also { println(brick) }
                }
        }
}

fun main() {
    val bricks = File("input.txt").useLines { it.toList() }.map { parseBrick(it) }.sortedBy { it.p1.z }

    bricks.joinToString("\n") { it.toString() }
        .also { println(it) }
}