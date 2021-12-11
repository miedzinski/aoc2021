fun Int(word: String): Int = word.fold(0) { acc, c -> acc or (1 shl (c - 'a')) }

val input = generateSequence(::readLine).map {
    val words = it.split(' ')
    val delimiterIndex = words.indexOf("|")
    val signals: List<Int> = words.subList(0, delimiterIndex).map { Int(it) }
    val output: List<Int> = words.subList(delimiterIndex + 1, words.size).map { Int(it) }

    Pair(signals, output)
}.toList()

fun part1(): Int {
    val counts = MutableList(10) { 0 }

    for ((_, output) in input) {
        for (digit in output) {
            when (digit.countOneBits()) {
                2 -> counts[1]++
                3 -> counts[7]++
                4 -> counts[4]++
                7 -> counts[8]++
            }
        }
    }

    return counts.sum()
}

println("part1: ${part1()}")

fun List<Int>.ofCount(length: Int): List<Int> = filter { it.countOneBits() == length }

fun Int.contains(other: Int) = and(other) == other

fun List<Int>.decode(): Map<Int, Int> {
    val digits = MutableList(10) { 0 }

    digits[1] = ofCount(2).single()
    digits[4] = ofCount(4).single()
    digits[7] = ofCount(3).single()
    digits[8] = ofCount(7).single()

    digits[9] = ofCount(6).single { it.contains(digits[4]) }
    digits[3] = ofCount(5).single { it.contains(digits[1]) }
    val e = digits[8] and digits[9].inv()
    digits[2] = ofCount(5).single { it.contains(e) }
    val b = digits[9] and digits[3].inv()
    digits[5] = ofCount(5).single { it.contains(b) }
    digits[6] = ofCount(6).single { it.contains(e) && !it.contains(digits[1]) }
    digits[0] = ofCount(6).single { it != digits[9] && it != digits[6] }

    return digits.withIndex().associate { (digit, segments) -> segments to digit }
}

fun part2(): Int = input.sumOf { (signals, output) ->
    val digits = signals.decode()
    output.asReversed()
        .withIndex()
        .sumOf { (idx, segments) ->
            val digit = digits[segments]!!
            val exp = generateSequence(1) { it * 10 }.elementAt(idx)
            digit * exp
        }
}

println("part2: ${part2()}")
