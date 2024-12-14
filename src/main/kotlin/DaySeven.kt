class DaySeven(val input: List<String>) {
  fun solveFirstChallenge(): Long {
    val equations = parseInput(input)
    return equations.filter { it.canReachTarget() }.sumOf { it.target }
  }

  private fun parseInput(input: List<String>): List<Equation> =
      input.map {
        val (targetStr, numbersStr) = it.split(":").map(String::trim)

        val target = targetStr.toLongOrNull()
        requireNotNull(target) { "Invalid target" }

        val numbersList = numbersStr.split(whitespaceRegex).mapNotNull(String::toLongOrNull)
        check(numbersList.isNotEmpty()) { "Empty numbers list" }

        Equation(target, numbersList)
      }
}

data class Equation(val target: Long, val numbers: List<Long>)

fun Equation.canReachTarget(): Boolean {
  if (numbers.size == 1) return target == numbers[0]
  if (target == 0L) numbers.contains(0)

  //  minTarget: filter out 1 (excluded by multiplication), sum the rest
  val minTarget = numbers.filter { it != 1L }.sum()

  // maxTarget: add 1s, multiply the rest
  val maxTarget = numbers.reduce { acc, next -> if (next == 1L) acc + next else acc * next }

  if (target < minTarget || target > maxTarget) return false
  if (numbers.contains(target)) return false
  if (minTarget == target || maxTarget == target) return true

  // Number of operator slots = numbers.size - 1
  val opCount = numbers.size - 1
  val totalCombos = 1 shl opCount // 2^(opCount)
  // Each bit in totalCombos represents + or *: 0 for +, 1 for *

  for (mask in 0 until totalCombos) {
    var result = numbers[0]
    for (i in 0 until opCount) {
      val nextNum = numbers[i + 1]
      val useMultiply = ((mask shr i) and 1) == 1
      result = if (useMultiply) result * nextNum else result + nextNum
    }
    if (result == target) return true
  }

  return false
}

fun main() {
  val daySeven = DaySeven(readInputAsLines("src/test/resources/daySeven.txt"))
  println(daySeven.solveFirstChallenge())
}
