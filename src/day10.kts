sealed class ParseResult {
    object Ok : ParseResult()
    data class Error(val char: Char) : ParseResult()
    data class Incomplete(val stack: List<Char>) : ParseResult()
}

fun Char.toClosing(): Char? = when (this) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> null
}

val parseResults = generateSequence(::readLine).map { line ->
    val stack = mutableListOf<Char>()
    for (char in line) {
        when (char) {
            '(', '[', '{', '<' -> stack.add(char.toClosing()!!)
            stack.removeLastOrNull() -> {}
            else -> return@map ParseResult.Error(char)
        }
    }
    when (stack.isEmpty()) {
        true -> ParseResult.Ok
        false -> ParseResult.Incomplete(stack)
    }
}.toList()

val part1 = parseResults.asSequence()
    .filterIsInstance<ParseResult.Error>()
    .sumOf {
        when (it.char) {
            ')' -> 3L
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

val part2 = parseResults.asSequence()
    .filterIsInstance<ParseResult.Incomplete>()
    .map {
        it.stack.asReversed().asSequence().fold(0L) { score, char ->
            score * 5 + when (char) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> 0
            }
        }
    }.sorted().toList().let {
        it[it.size / 2]
    }

println("part1: $part1")
println("part2: $part2")
