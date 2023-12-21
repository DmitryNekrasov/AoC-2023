import java.io.File

fun getStart(field: List<String>) =
    field.withIndex().filter { (_, line) -> 'S' in line }.map { (i, line) -> i to line.indexOf('S') }.first()

fun solve(field: List<String>): Int {
    val (startRow, startCol) = getStart(field)

    println(field.joinToString("\n"))
    println("startRow = $startRow, startCol = $startCol")

    return -1
}

fun main() {
    val field = File("input_simple_1.txt").useLines { it.toList() }
    println(solve(field))
}