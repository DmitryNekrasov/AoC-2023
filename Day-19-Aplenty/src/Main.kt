import java.io.File

fun main() {
    val input = File("input_simple_1.txt").useLines { it.toList() }
    println(input)
}