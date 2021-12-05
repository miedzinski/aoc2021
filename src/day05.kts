data class Point(val x: Int, val y: Int)

class Line(val from: Point, val to: Point) {
    fun isHorizontalOrVertical(): Boolean =
        from.x == to.x || from.y == to.y

    fun points(): Sequence<Point> {
        fun diff(selector: (Point) -> Int): Int {
            val from = selector(from)
            val to = selector(to)
            return when {
                from < to -> 1
                from > to -> -1
                else -> 0
            }
        }

        val dx = diff { it.x }
        val dy = diff { it.y }

        return generateSequence(from) {
            with(it) { Point(x + dx, y + dy) }
        }.takeWhile { it != to }
    }
}

object Grid {
    val points = mutableMapOf<Point, Int>().withDefault { 0 }

    fun insert(point: Point) {
        points[point] = points.getValue(point) + 1
    }

    fun countOverlapping(): Int = points.count { it.value > 1 }
}

val linePattern = "(\\d+),(\\d+) -> (\\d+),(\\d+)".toRegex()
val (horizontalsAndVerticals, diagonals) = generateSequence(::readLine).map {
    val (x1, y1, x2, y2) = linePattern
        .matchEntire(it)!!
        .destructured
        .toList()
        .map(String::toInt)
    Line(Point(x1, y1), Point(x2, y2))
}.partition { it.isHorizontalOrVertical() }

fun solve(lines: List<Line>): Int {
    lines.asSequence()
        .flatMap(Line::points)
        .forEach(Grid::insert)
    return Grid.countOverlapping()
}

println("part1: ${solve(horizontalsAndVerticals)}")
println("part2: ${solve(diagonals)}")
