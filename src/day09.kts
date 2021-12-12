val map = generateSequence(::readLine).map {
    it.map(Char::digitToInt)
}.toList()

typealias HeightMap = List<List<Int>>

data class Point(val x: Int, val y: Int)

operator fun HeightMap.get(point: Point): Int = this[point.x][point.y]

fun HeightMap.points(): Sequence<Point> =
    (0 until size)
        .asSequence()
        .flatMap { x ->
            (0 until map[x].size).asSequence().map { y -> Point(x, y) }
        }

fun Point.adjacent(): Sequence<Point> = sequenceOf(
    copy(x - 1, y),
    copy(x + 1, y),
    copy(x, y - 1),
    copy(x, y + 1),
).filter { (x, y) ->
    x in 0 until map.size && y in 0 until map[x].size
}

fun Point.isLow(): Boolean = adjacent().all { map[it] > map[this] }

fun HeightMap.lowPoints(): Sequence<Point> = points().filter { it.isLow() }

fun HeightMap.basins(): Sequence<Sequence<Point>> =
    lowPoints().map { start ->
        val visited = mutableSetOf(start)
        val queue = mutableListOf(start)
        generateSequence(queue::removeLastOrNull)
            .onEach { point ->
                point.adjacent()
                    .filterNot(visited::contains)
                    .filter {
                        map[point] < map[it] && map[it] < 9
                    }
                    .onEach { visited.add(it) }
                    .toCollection(queue)
            }
    }

val part1 = map.lowPoints().sumOf { map[it] + 1 }
val part2 = map.basins()
    .map { it.count() }
    .sortedDescending()
    .take(3)
    .reduce(Int::times)

println("part1: $part1")
println("part2: $part2")
