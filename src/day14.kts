data class Rule(val pair: Pair<Char, Char>, val insertion: Char)

fun <K> MutableMap<K, Long>.increment(key: K, count: Long = 1) {
    set(key, getOrDefault(key, 0) + count)
}

val elements = mutableMapOf<Char, Long>()
val pairs = readLine()!!.asSequence()
    .onEach { elements.increment(it) }
    .windowed(2) { (first, second) -> Pair(first, second) }
    .groupingBy { it }
    .eachCount()
    .mapValuesTo(mutableMapOf()) { (_, count) -> count.toLong() }
readLine()
val ruleRegex = "([A-Z])([A-Z]) -> ([A-Z])".toRegex()
val rules = generateSequence(::readLine).map { line ->
    val (left, right, insertion) = ruleRegex.matchEntire(line)!!.destructured
    Rule(left.single() to right.single(), insertion.single())
}.toList()

fun tick() {
    val toDelete = mutableSetOf<Pair<Char, Char>>()
    val toInsert = mutableMapOf<Pair<Char, Char>, Long>()
    rules.asSequence()
        .filter { it.pair in pairs }
        .forEach {
            val count = pairs[it.pair]!!
            toDelete.add(it.pair)
            toInsert.increment(Pair(it.pair.first, it.insertion), count)
            toInsert.increment(Pair(it.insertion, it.pair.second), count)
            elements.increment(it.insertion, count)
        }
    toDelete.forEach(pairs::remove)
    toInsert.forEach {
        pairs.increment(it.key, it.value)
    }
}

fun solve(steps: Int): Long {
    repeat(steps) { tick() }
    return elements.values.maxOf { it } - elements.values.minOf { it }
}

println("part1: ${solve(10)}")
println("part2: ${solve(30)}")
