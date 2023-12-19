import java.io.File

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

abstract class Rule(val nextState: String) {
    abstract fun isAccepted(part: Part): Boolean
}

class RuleWithCondition(private val category: Char, private val comparisonSign: Char, private val value: Int, nextState: String) : Rule(nextState) {
    override fun isAccepted(part: Part): Boolean {
        val x = when (category) {
            'x' -> part.x
            'm' -> part.m
            'a' -> part.a
            's' -> part.s
            else -> throw RuntimeException()
        }
        return if (comparisonSign == '>') x > value else x < value
    }

    override fun toString() = "$category$comparisonSign$value:$nextState"
}

class AlwaysTrueRule(nextState: String): Rule(nextState) {
    override fun isAccepted(part: Part) = true

    override fun toString() = nextState
}

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