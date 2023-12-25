import java.io.File

fun parseLine(line: String): Pair<String, Set<String>> {
    val (from, toList) = line.split(": ")
    val to = toList.split(" ").toSet()
    return from to to
}

fun main() {
    val graph = File("input_simple_1.txt").useLines { it.toList() }.map { parseLine(it) }
        .associateBy({ it.first }, { it.second })
    graph.toList().joinToString("\n") { it.toString() }
        .also { println(it) }
}