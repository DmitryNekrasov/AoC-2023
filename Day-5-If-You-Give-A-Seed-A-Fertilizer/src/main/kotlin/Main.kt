import java.io.File

data class Range(val start: Long, val length: Long) {
    operator fun contains(value: Long): Boolean = value in start..<start + length
}

data class Mapping(val offset: Long, val range: Range)

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    val seeds = input[0].split(":")[1].trim().split(" ").map { it.toLong() }
    var inputIndex = 1
    val relationship = HashMap<String, String>()
    val typeToMappings = HashMap<String, List<Mapping>>()
    while (++inputIndex < input.size) {
        val (source, dest) = input[inputIndex].split(" ")[0].split("-to-")
        relationship[source] = dest
        val mappings = mutableListOf<Mapping>()
        while (++inputIndex < input.size && input[inputIndex] != "") {
            val (dstStart, srcStart, length) = input[inputIndex].split(" ").map { it.toLong() }
            val mapping = Mapping(dstStart - srcStart, Range(srcStart, length))
            mappings.add(mapping)
        }
        typeToMappings[source] = mappings
    }

    val locations = mutableListOf<Long>()
    for (seed in seeds) {
        var type = "seed"
        var value = seed
        while (type != "location") {
            val mappings = typeToMappings[type]!!
            var nextValue = value
            for (mapping in mappings) {
                if (value in mapping.range) {
                    nextValue = value + mapping.offset
                    break
                }
            }
            value = nextValue
            type = relationship[type]!!
        }
        locations.add(value)
    }
    val ansPart1 = locations.min()
    println(ansPart1)
}