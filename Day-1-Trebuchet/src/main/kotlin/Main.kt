import java.io.File

fun main() {
    File("input.txt")
        .useLines { it.toList() }
        .sumOf { s -> s.first { it.isDigit() }.digitToInt() * 10 + s.last { it.isDigit() }.digitToInt() }
        .also { println(it) }
}