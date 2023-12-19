import java.io.File
import java.util.LinkedList
import java.util.Queue

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

data class PartRange(val xRange: IntRange, val mRange: IntRange, val aRange: IntRange, val sRange: IntRange) {
    val number: Long
        get() = (xRange.last - xRange.first + 1).toLong() *
                (mRange.last - mRange.first + 1).toLong() *
                (aRange.last - aRange.first + 1).toLong() *
                (sRange.last - sRange.first + 1).toLong()

    fun splitBy(x: Char, value: Int): Pair<PartRange, PartRange> {
        return when (x) {
            'x' -> copy(xRange = xRange.first..<value) to copy(xRange = value..xRange.last)
            'm' -> copy(mRange = mRange.first..<value) to copy(mRange = value..mRange.last)
            'a' -> copy(aRange = aRange.first..<value) to copy(aRange = value..aRange.last)
            's' -> copy(sRange = sRange.first..<value) to copy(sRange = value..sRange.last)
            else -> throw RuntimeException()
        }
    }

    companion object {
        private const val LIMIT = 4000
        val INITIAL_RANGE = PartRange(1..LIMIT, 1..LIMIT, 1..LIMIT, 1..LIMIT)
        val EMPTY_RANGE = PartRange(0..0, 0..0, 0..0, 0..0)
    }
}

data class Workflow(val rules: List<Rule>) {
    fun lastState(part: Part): String {
        for (rule in rules) {
            if (rule.isAccepted(part)) {
                return rule.nextState
            }
        }
        throw RuntimeException()
    }

    fun nextRanges(partRange: PartRange): List<Pair<String, PartRange>> {
        val result = mutableListOf<Pair<String, PartRange>>()
        var range = partRange
        for (rule in rules) {
            val (trueRange, falseRange) = rule.splitRange(range)
            if (trueRange != PartRange.EMPTY_RANGE) {
                result.add(rule.nextState to trueRange)
            }
            range = falseRange
        }
        return result
    }
}

abstract class Rule(val nextState: String) {
    abstract fun isAccepted(part: Part): Boolean
    protected abstract fun isAccepted(partRange: PartRange): Boolean
    abstract fun splitRange(partRange: PartRange): Pair<PartRange, PartRange> // true, false
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

    override fun isAccepted(partRange: PartRange): Boolean {
        return when (category) {
            'x' -> value in partRange.xRange
            'm' -> value in partRange.mRange
            'a' -> value in partRange.aRange
            's' -> value in partRange.sRange
            else -> throw RuntimeException()
        }
    }

    override fun splitRange(partRange: PartRange): Pair<PartRange, PartRange> {
        if (!isAccepted(partRange)) return PartRange.EMPTY_RANGE to partRange
        if (comparisonSign == '<') return partRange.splitBy(category, value)
        val (r1, r2) = partRange.splitBy(category, value + 1)
        return r2 to r1
    }

    override fun toString() = "$category$comparisonSign$value:$nextState"
}

class AlwaysTrueRule(nextState: String) : Rule(nextState) {
    override fun isAccepted(part: Part) = true
    override fun isAccepted(partRange: PartRange) = true
    override fun splitRange(partRange: PartRange) = partRange to PartRange.EMPTY_RANGE
    override fun toString() = nextState
}

fun parseWorkflowStrings(workflowStrings: List<String>): Map<String, Workflow> {
    fun parseWorkflowString(workflowString: String): Pair<String, Workflow> {
        fun parseRules(rules: List<String>): List<Rule> {
            fun parseRule(ruleString: String): Rule {
                if (':' in ruleString) {
                    val (condition, nextWorkflow) = ruleString.split(":")
                    return RuleWithCondition(
                        condition[0], condition[1], condition.substring(2, condition.length).toInt(), nextWorkflow
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
        val (x, m, a, s) = partString.substring(1..<partString.lastIndex).split(",").map { it.split("=").last() }.map { it.toInt() }
        return Part(x, m, a, s)
    }
    return partStrings.map { parsePartString(it) }
}

fun solvePartOne(workflows: Map<String, Workflow>, parts: List<Part>): Int {
    return parts.filter { it.isAcceptedByWorkflows(workflows) }.sumOf { it.rating }
}

fun solvePartTwo(workflows: Map<String, Workflow>): Long {
    val queue: Queue<Pair<String, PartRange>> = LinkedList()
    queue.offer("in" to PartRange.INITIAL_RANGE)
    var ans = 0L
    while (queue.isNotEmpty()) {
        val (wfName, range) = queue.poll()
        if (wfName == "R") continue
        if (wfName == "A") {
            ans += range.number
            continue
        }
        queue.addAll(workflows[wfName]!!.nextRanges(range))
    }
    return ans
}

fun main() {
    val input = File("input.txt").useLines { it.toList() }
    val delimiter = input.indexOf("")
    val workflowStrings = input.subList(0, delimiter)
    val partStrings = input.subList(delimiter + 1, input.size)
    val workflows = parseWorkflowStrings(workflowStrings)
    val parts = parsePartStrings(partStrings)
    println(solvePartOne(workflows, parts))
    println(solvePartTwo(workflows))
}