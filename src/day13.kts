data class Point(val x: Int, val y: Int)
enum class Axis { X, Y }
data class Fold(val axis: Axis, val position: Int)

val dots = generateSequence(::readLine)
    .takeWhile(String::isNotEmpty)
    .map { it.split(',').map(String::toInt).let { Point(it[0], it[1]) } }
    .toSet()

val foldRegex = "fold along ([xy])=(\\d+)".toRegex()
val folds = generateSequence(::readLine)
    .map { line ->
        val (axis, position) = foldRegex.matchEntire(line)!!.destructured
        Fold(Axis.valueOf(axis.uppercase()), position.toInt())
    }.toList()

fun Point.select(axis: Axis): Int = when (axis) {
    Axis.X -> x
    Axis.Y -> y
}

fun Point.transform(fold: Fold): Point {
    val oldPosition = select(fold.axis)
    val newPosition = 2 * fold.position - oldPosition
    return when (fold.axis) {
        Axis.X -> copy(x = newPosition)
        Axis.Y -> copy(y = newPosition)
    }
}

fun Set<Point>.apply(fold: Fold): Set<Point> {
    val (unchanged, toMirror) = partition {
        it.select(fold.axis) < fold.position
    }
    val mirrored = toMirror.asSequence().map { it.transform(fold) }.toList()
    return unchanged union mirrored
}

fun Set<Point>.asGrid(): String {
    val maxX = maxOf { it.x }
    val maxY = maxOf { it.y }
    val builder = StringBuilder()

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            val mark = when (Point(x, y)) {
                in this -> '#'
                else -> '.'
            }
            builder.append(mark)
        }
        builder.append('\n')
    }

    return builder.toString()
}

val part1 = dots.apply(folds.first()).size

println("part1: $part1")
println("part2:")

folds.fold(dots) { acc, fold ->
    acc.apply(fold)
}.asGrid()
