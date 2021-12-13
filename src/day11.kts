val grid = generateSequence(::readLine).flatMap {
    it.asSequence().map(Char::digitToInt)
}.toMutableList()

val gridSize = 10

typealias Grid = MutableList<Int>

data class Point(val x: Int, val y: Int)

operator fun Grid.get(point: Point): Int =
    get(point.y * gridSize + point.x)

operator fun Grid.set(point: Point, value: Int): Int =
    set(point.y * gridSize + point.x, value)

fun Grid.points(): Sequence<Point> =
    (0 until gridSize)
        .asSequence()
        .flatMap { x ->
            (0 until gridSize).asSequence().map { y -> Point(x, y) }
        }

fun Point.adjacent(): Sequence<Point> = sequenceOf(
    copy(x - 1, y),
    copy(x - 1, y - 1),
    copy(x - 1, y + 1),
    copy(x, y - 1),
    copy(x, y + 1),
    copy(x + 1, y - 1),
    copy(x + 1, y),
    copy(x + 1, y + 1),
).filter { (x, y) ->
    fun check(n: Int): Boolean = n in 0 until gridSize
    check(x) && check(y)
}

fun Grid.tick(): Int {
    val queue = grid.points().filter { grid[it] == 9 }.toMutableList()
    val visited = queue.toMutableSet()

    grid.replaceAll { it + 1 }

    while (queue.isNotEmpty()) {
        with(queue.removeLast()) {
            adjacent()
                .filterNot(visited::contains)
                .onEach { grid[it]++ }
                .filter { grid[it] > 9 }
                .onEach { visited.add(it) }
                .toCollection(queue)
        }
    }

    grid.replaceAll {
        when (it) {
            in 0..9 -> it
            else -> 0
        }
    }

    return visited.size
}

val nSteps = 100
var part1: Int = 0
var part2: Int? = null

(1..Int.MAX_VALUE).asSequence()
    .onEach { step ->
        val flashed = grid.tick()
        if (step in 1..nSteps) {
            part1 += flashed
        }
        if (part2 == null && flashed == grid.size) {
            part2 = step
        }
    }
    .takeWhile {
        it <= nSteps || part2 == null
    }
    .last()

println("part1: $part1")
println("part2: $part2")
