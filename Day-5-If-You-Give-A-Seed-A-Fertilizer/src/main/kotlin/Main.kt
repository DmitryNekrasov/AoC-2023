import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.min

data class Range(val start: Long, val length: Long) {
    operator fun contains(value: Long): Boolean = value in start..<start + length
}

data class Mapping(val offset: Long, val range: Range)

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    val seeds = input[0].split(":")[1].trim().split(" ").map { it.toLong() }
    var inputIndex = 1
    val relationship = ConcurrentHashMap<String, String>()
    val typeToMappings = ConcurrentHashMap<String, ConcurrentLinkedQueue<Mapping>>()
    while (++inputIndex < input.size) {
        val (source, dest) = input[inputIndex].split(" ")[0].split("-to-")
        relationship[source] = dest
        val mappings = ConcurrentLinkedQueue<Mapping>()
        while (++inputIndex < input.size && input[inputIndex] != "") {
            val (dstStart, srcStart, length) = input[inputIndex].split(" ").map { it.toLong() }
            val mapping = Mapping(dstStart - srcStart, Range(srcStart, length))
            mappings.add(mapping)
        }
        typeToMappings[source] = mappings
    }

    var ansPart1 = Long.MAX_VALUE
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
        ansPart1 = min(ansPart1, value)
    }
    println(ansPart1)

    val seedRanges = mutableListOf<Range>()
    for (i in seeds.indices step 2) {
        seedRanges.add(Range(seeds[i], seeds[i + 1]))
    }
    println("Range number: ${seedRanges.size}")

    val results = ConcurrentLinkedQueue<Long>()
    val threads = mutableListOf<Thread>()
    for (range in seedRanges) {
        val thread = Thread {
            var ans = Long.MAX_VALUE
            for (seed in range.start..<range.start + range.length) {
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
                ans = min(ans, value)
            }
            results.add(ans)
        }
        threads.add(thread)
    }
    for (thread in threads) {
        thread.start()
    }
    for (thread in threads) {
        thread.join()
    }
    val ansPart2 = results.min()
    println(ansPart2)
}
