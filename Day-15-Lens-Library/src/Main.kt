import java.io.File

const val BASE = 17
const val SIZE = 256

fun String.hash() = this.fold(0) { acc, c -> (acc + c.code) * BASE % SIZE }

fun solvePartOne(steps: List<String>) = steps.sumOf { it.hash() }

fun solvePartTwo(steps: List<String>): Int {
    val boxes = Array(SIZE) { LinkedHashMap<String, Int>() }
    for (step in steps) {
        if (step.last() == '-') {
            val label = step.substring(0, step.lastIndex)
            boxes[label.hash()].remove(label)
        } else {
            val (label, focalLengthStr) = step.split("=")
            val focalLength = focalLengthStr.toInt()
            boxes[label.hash()][label] = focalLength
        }
    }
    var result = 0
    for ((boxIndex, lenses) in boxes.withIndex()) {
        var slotIndex = 0
        for (focalLength in lenses.values) {
            result += (boxIndex + 1) * ++slotIndex * focalLength
        }
    }
    return result
}

fun main() {
    val steps = File("input.txt").useLines { it.toList() }.first().split(",")
    println(solvePartOne(steps))
    println(solvePartTwo(steps))
}