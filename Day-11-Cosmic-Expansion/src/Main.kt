import java.io.File

fun main() {
    val image = File("input_simple_1.txt").useLines { it.toList() }
    image.joinToString("\n")
        .also { println(it) }
}