enum class Command { FORWARD, DOWN, UP }

val input = generateSequence(::readLine).map {
    val split = it.split(" ")
    val command = when (split.first()) {
        "forward" -> Command.FORWARD
        "down" -> Command.DOWN
        "up" -> Command.UP
        else -> throw IllegalArgumentException()
    }
    val units = split.last().toInt()
    Pair(command, units)
}.toList()

fun part1(): Int {
    var horizontal = 0
    var depth = 0
    for ((command, units) in input) {
        when (command) {
            Command.FORWARD -> horizontal += units
            Command.DOWN -> depth += units
            Command.UP -> depth -= units
        }
    }
    return horizontal * depth
}

println("part1: ${part1()}")

fun part2(): Int {
    var horizontal = 0
    var depth = 0
    var aim = 0
    for ((command, units) in input) {
        when (command) {
            Command.FORWARD -> {
                horizontal += units
                depth += aim * units
            }
            Command.DOWN -> aim += units
            Command.UP -> aim -= units
        }
    }
    return horizontal * depth
}

println("part2: ${part2()}")
