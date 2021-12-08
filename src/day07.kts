import kotlin.math.abs

val input = readLine()!!.split(',').map(String::toInt).sorted().toList()

val median = input[input.size / 2]
val part1 = input.sumOf { abs(it - median) }

println("part1: $part1")

fun sum(n: Int): Int = n * (1 + n) / 2

val average = input.average().toInt()
val part2 = input.sumOf { sum(abs(it - average)) }

println("part2: $part2")
