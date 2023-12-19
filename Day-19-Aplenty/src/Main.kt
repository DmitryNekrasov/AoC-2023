import java.io.File

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

fun parsePartStrings(partStrings: List<String>): List<Part> {
    fun parsePartString(partString: String): Part {
        val (x, m, a, s) = partString.substring(1..<partString.lastIndex)
            .split(",")
            .map { it.split("=").last() }
            .map { it.toInt() }
        return Part(x, m, a, s)
    }
    return partStrings.map { parsePartString(it) }
}

fun main() {
    val input = File("input_simple_1.txt").useLines { it.toList() }
    val delimiter = input.indexOf("")
    val workflowStrings = input.subList(0, delimiter)
    val partStrings = input.subList(delimiter + 1, input.lastIndex)

    workflowStrings.joinToString("\n")
        .also { println(it) }
    println("--")

    val parts = parsePartStrings(partStrings)
    parts.joinToString("\n") { it.toString() }
        .also { println(it) }
}