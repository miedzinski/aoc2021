sealed class Cave {
    val paths = mutableSetOf<Cave>()

    object Start : Cave()
    object End : Cave()
    data class Small(val id: String) : Cave()
    data class Big(val id: String) : Cave()
}

val allCaves = mutableMapOf<String, Cave>(
    "start" to Cave.Start,
    "end" to Cave.End,
)

generateSequence(::readLine).forEach {
    val (first, second) = it.split('-').map {
        allCaves.getOrPut(it) {
            when (it.all(Char::isUpperCase)) {
                true -> Cave.Big(it)
                else -> Cave.Small(it)
            }
        }
    }
    first.paths.add(second)
    second.paths.add(first)
}

interface Strategy {
    fun next(cave: Cave): Sequence<Strategy>
}

class SmallAtMostOnceStrategy(val visited: Set<Cave>) : Strategy {
    override fun next(cave: Cave): Sequence<Strategy> = when (cave) {
        is Cave.Big, !in visited -> sequenceOf(SmallAtMostOnceStrategy(visited union setOf(cave)))
        else -> emptySequence()
    }
}

class SingleSmallTwice(val visited: Set<Cave>) : Strategy {
    override fun next(cave: Cave): Sequence<Strategy> = when (cave) {
        is Cave.Big, !in visited -> sequenceOf(SingleSmallTwice(visited union setOf(cave)))
        is Cave.Small -> {
            val nextVisited = visited union setOf(cave)
            val next = sequenceOf(SmallAtMostOnceStrategy(nextVisited))
            when (cave) {
                in visited -> next
                else -> next + sequenceOf(SingleSmallTwice(nextVisited))
            }
        }
        else -> emptySequence()
    }
}

fun visit(cave: Cave, strategy: Strategy): Int = when (cave) {
    is Cave.End -> 1
    else -> cave.paths.asSequence()
        .flatMap { nextCave -> strategy.next(nextCave).map { nextCave to it } }
        .sumOf { (cave, strategy) -> visit(cave, strategy) }
}

val part1 = visit(Cave.Start, SmallAtMostOnceStrategy(setOf(Cave.Start)))
val part2 = visit(Cave.Start, SingleSmallTwice(setOf(Cave.Start)))

println("part1: $part1")
println("part2: $part2")
