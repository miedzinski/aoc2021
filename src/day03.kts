val line = readLine()!!
val nBits = line.length
val input = (sequenceOf(line) + generateSequence(::readLine)).map { it.toInt(2) }.toList()

fun mostCommon(numbers: List<Int>, nthBit: Int): Int? =
    numbers.count { it and (1 shl nthBit) != 0 }.let {
        when {
            2 * it > numbers.size -> 1 shl nthBit
            2 * it < numbers.size -> 0
            else -> null
        }
    }

fun leastCommon(numbers: List<Int>, nthBit: Int): Int? =
    mostCommon(numbers, nthBit)?.inv()?.and(1 shl nthBit)

fun part1(criteria: (Int) -> Int?): Int =
    (0 until nBits).fold(0) { acc, nthBit -> acc or (criteria(nthBit) ?: 0) }

val gamma = part1 { mostCommon(input, it) }
val epsilon = part1 { leastCommon(input, it) }

println("part1: ${gamma * epsilon}")

fun part2(criteria: (List<Int>, Int) -> Int): Int =
    ((nBits - 1) downTo 0).fold(input) { acc, nthBit ->
        if (acc.size > 1) {
            val mask = criteria(acc, nthBit)
            acc.filter { it and (1 shl nthBit) == mask }
        } else {
            acc
        }
    }.single()

val oxygen = part2 { numbers, nthBit -> mostCommon(numbers, nthBit) ?: 1 shl nthBit }
val co2 = part2 { numbers, nthBit -> leastCommon(numbers, nthBit) ?: 0 }

println("part2: ${oxygen * co2}")
