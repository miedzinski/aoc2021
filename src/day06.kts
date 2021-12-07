val fish = MutableList<Long>(9) { 0 }

readLine()!!.split(',')
    .forEach { fish[it.toInt()]++ }

fun tick() {
    fish += fish.removeAt(0)
    fish[6] += fish[8]
}

repeat(80) { tick() }

println("part1: ${fish.sum()}")

repeat(256 - 80) { tick() }

println("part2: ${fish.sum()}")
