import java.io.File

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    val rating: Int
        get() = x + m + a + s

    fun isAcceptedByWorkflows(workflows: Map<String, Workflow>): Boolean {
        var currentWFName = "in"
        while (currentWFName != "A" && currentWFName != "R") {
            currentWFName = workflows[currentWFName]!!.lastState(this)
        }
        return currentWFName == "A"
    }
}

data class PartRange(val xRange: IntRange, val mRange: IntRange, val aRange: IntRange, val sRange: IntRange)

data class Workflow(val rules: List<Rule>) {
    fun lastState(part: Part): String {
        for (rule in rules) {
            if (rule.isAccepted(part)) {
                return rule.nextState
            }
        }
        throw RuntimeException()
    }
}

abstract class Rule(val nextState: String) {
    abstract fun isAccepted(part: Part): Boolean
}

class RuleWithCondition(
    private val category: Char,
    private val comparisonSign: Char,
    private val value: Int,
    nextState: String
) : Rule(nextState) {
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

class AlwaysTrueRule(nextState: String) : Rule(nextState) {
    override fun isAccepted(part: Part) = true

    override fun toString() = nextState
}

fun parseWorkflowStrings(workflowStrings: List<String>): Map<String, Workflow> {
    fun parseWorkflowString(workflowString: String): Pair<String, Workflow> {
        fun parseRules(rules: List<String>): List<Rule> {
            fun parseRule(ruleString: String): Rule {
                if (':' in ruleString) {
                    val (condition, nextWorkflow) = ruleString.split(":")
                    return RuleWithCondition(
                        condition[0],
                        condition[1],
                        condition.substring(2, condition.length).toInt(),
                        nextWorkflow
                    )
                }
                return AlwaysTrueRule(ruleString)
            }
            return rules.map { parseRule(it) }
        }
        val (name, ruleStrings) = workflowString.substring(0..<workflowString.lastIndex).split("{")
        return name to Workflow(parseRules(ruleStrings.split(",")))
    }
    return workflowStrings.map { parseWorkflowString(it) }.associateBy({ it.first }, { it.second })
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

fun solvePartOne(workflows: Map<String, Workflow>, parts: List<Part>): Int {
    return parts.filter { it.isAcceptedByWorkflows(workflows) }.sumOf { it.rating }
}
fun solvePartTwo(workflows: Map<String, Workflow>): Long {
    return -1L
}

fun main() {
    val input = File("input.txt").useLines { it.toList() }
    val delimiter = input.indexOf("")
    val workflowStrings = input.subList(0, delimiter)
    val partStrings = input.subList(delimiter + 1, input.size)
    val workflows = parseWorkflowStrings(workflowStrings)
    val parts = parsePartStrings(partStrings)
    println(solvePartOne(workflows, parts))
}