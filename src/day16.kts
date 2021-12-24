enum class PacketType(val id: Int) {
    SUM(0),
    PRODUCT(1),
    MINIMUM(2),
    MAXIMUM(3),
    LITERAL(4),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUALS(7),
}

class BitStream(private val chars: String) {
    private val RADIX: Int = 16
    private val BITS_PER_CHAR = 4
    val bits = chars.asSequence().map {
        it.digitToInt(RADIX).run(Integer::toBinaryString).padStart(BITS_PER_CHAR, '0')
    }.joinToString("")
    var position: Int = 0
        private set

    fun read(count: Int): Int =
        bits.substring(position until (position + count))
            .toInt(2)
            .also { position += count }
}

val stream = BitStream(readLine()!!)
var part1 = 0L

fun BitStream.readPacket(): Long {
    part1 += stream.read(3)
    return when (stream.read(3)) {
        PacketType.LITERAL.id -> stream.readLiteral()
        PacketType.SUM.id -> stream.readSubPackets().sumOf { it }
        PacketType.PRODUCT.id -> stream.readSubPackets().reduce(Long::times)
        PacketType.MINIMUM.id -> stream.readSubPackets().minOf { it }
        PacketType.MAXIMUM.id -> stream.readSubPackets().maxOf { it }
        PacketType.GREATER_THAN.id -> stream.readSubPackets().compare { a, b -> a > b }
        PacketType.LESS_THAN.id -> stream.readSubPackets().compare { a, b -> a < b }
        PacketType.EQUALS.id -> stream.readSubPackets().compare { a, b -> a == b }
        else -> throw IllegalStateException()
    }
}

fun BitStream.readLiteral(): Long {
    val groupSize = 4
    return sequence {
        do {
            val shouldContinue = stream.read(1) == 1
            yield(stream.read(groupSize))
        } while (shouldContinue)
    }.fold(0L) { acc, elt ->
        acc shl groupSize or elt.toLong()
    }
}

fun BitStream.readSubPackets(): Sequence<Long> {
    return when (stream.read(1)) {
        0 -> {
            val subPacketsLength = stream.read(15)
            val start = stream.position
            sequence {
                while (stream.position < start + subPacketsLength) {
                    yield(stream.readPacket())
                }
            }
        }
        1 -> generateSequence { stream.readPacket() }.take(stream.read(11))
        else -> throw IllegalStateException()
    }
}

fun <T : Comparable<T>> Sequence<T>.compare(operator: (T, T) -> Boolean): Long =
    toList().let { (first, second) ->
        when {
            operator(first, second) -> 1
            else -> 0
        }
    }

val part2 = stream.readPacket()

println("part1: $part1")
println("part2: $part2")
