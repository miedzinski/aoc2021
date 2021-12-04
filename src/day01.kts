val input = generateSequence(::readLine).map(String::toInt).toList()

fun solve(windowSize: Int): Int =
    input.windowed(windowSize) { it.sum() }.windowed(2).count { it.first() < it.last() }

println("part1: ${solve(1)}")
println("part2: ${solve(3)}")
