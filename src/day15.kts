import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int)

fun Point.neighboursIn(cave: List<List<Int>>): Sequence<Point> = sequenceOf(
    copy(x - 1, y),
    copy(x + 1, y),
    copy(x, y - 1),
    copy(x, y + 1),
).filter {
    with(it) {
        y in 0 until cave.size && x in 0 until cave[y].size
    }
}

operator fun List<List<Int>>.get(point: Point): Int = this[point.y][point.x]
operator fun List<MutableList<Int>>.set(point: Point, value: Int): Int =
    get(point.y).set(point.x, value)

fun List<List<Int>>.lowestTotalRisk(): Int {
    val start = Point(0, 0)
    val end = size.minus(1).let { Point(it, get(it).size - 1) }
    val visited = mutableSetOf<Point>()
    val totalCosts = map {
        it.asSequence().map { Int.MAX_VALUE }.toMutableList()
    }.also { it[start] = 0 }

    val queue = PriorityQueue(size, object : Comparator<Point> {
        override fun compare(o1: Point, o2: Point): Int =
            totalCosts[o1].compareTo(totalCosts[o2])
    }).also { it.add(start) }

    generateSequence(queue::poll)
        .takeWhile { it != end }
        .filterNot(visited::contains)
        .onEach { point ->
            val currentCost = totalCosts[point]
            point.neighboursIn(this).onEach {
                totalCosts[it] = min(totalCosts[it], currentCost + get(it))
            }.toCollection(queue)
        }.toCollection(visited)

    return totalCosts[end]
}

val cave = generateSequence(::readLine)
    .map { it.map(Char::digitToInt) }
    .toList()

println("part1: ${cave.lowestTotalRisk()}")

val expansion = 5
val entireCave = List(expansion * cave.size) { y ->
    List(expansion * cave[0].size) { x ->
        val original = cave[Point(x % cave[0].size, y % cave.size)]
        val increase = y / cave.size + x / cave[0].size
        generateSequence(original) { max((it + 1) % 10, 1) }
            .elementAt(increase)
    }
}

println("part2: ${entireCave.lowestTotalRisk()}")
