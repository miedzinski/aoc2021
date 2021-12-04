data class Board(val rows: List<List<Int>>) {
    val columns: List<List<Int>> by lazy {
        (0 until rows.size).map { col -> (0 until rows.size).map { row -> rows[row][col] } }
    }
}

val boardSize = 5

val allDraws = readLine()!!.split(',').map(String::toInt).toList()
var boards = generateSequence(::readLine).chunked(boardSize + 1) {
    it.asSequence()
        .drop(1)
        .map { it.trimStart().split("\\s+".toRegex()).map(String::toInt) }
        .toList()
        .let { Board(it) }
}.toList()

val wins = sequence {
    val draws = mutableSetOf<Int>()
    for (num in allDraws) {
        draws.add(num)
        val (winningBoards, others) = boards.partition {
            (it.rows + it.columns).any(draws::containsAll)
        }
        boards = others
        winningBoards.forEach {
            yield(it.rows.flatten().filter { !draws.contains(it) }.sum() * num)
        }
    }
}

println("part1: ${wins.first()}")
println("part2: ${wins.last()}")
