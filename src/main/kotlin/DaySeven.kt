class DaySeven(val input: List<String>) {
  private val addMultiplyOperations = listOf(Operation.Add(), Operation.Multiply())

  private val addMultiplyConcatenateOperations =
      listOf(Operation.Add(), Operation.Multiply(), Operation.Concatenate())

  fun solveFirstChallenge(): Long =
      parseEquations(input).filter { it.canReachTarget(addMultiplyOperations) }.sumOf { it.target }

  fun solveSecondChallenge(): Long =
      parseEquations(input)
          .partition { it.canReachTarget(addMultiplyOperations) }
          .let { (solvedByAdditionAndMultiplication, rest) ->
            solvedByAdditionAndMultiplication.sumOf { it.target } +
                rest
                    .filter { it.canReachTarget(addMultiplyConcatenateOperations) }
                    .sumOf { it.target }
          }

  private fun parseEquations(input: List<String>): List<Equation> =
      input.map {
        val (targetStr, numbersStr) = it.split(":").map(String::trim)

        val target = targetStr.toLongOrNull()
        requireNotNull(target) { "Invalid target" }

        val numbersList = numbersStr.split(whitespaceRegex).mapNotNull(String::toLongOrNull)
        check(numbersList.isNotEmpty()) { "Empty numbers list" }

        Equation(target, numbersList)
      }
}

data class Equation(val target: Long, val numbers: List<Long>) {
  fun canReachTarget(operators: List<Operation>): Boolean {
    if (numbers.size == 1) return target == numbers.first()

    val initialCheckResult = performInitialChecks(target, numbers, operators)
    if (initialCheckResult != null) return initialCheckResult

    return numbers
        .drop(1)
        .fold(mutableSetOf(numbers.first())) { acc, nextNumber ->
          acc.flatMap { value -> operators.map { it.apply(value, nextNumber) } }.toMutableSet()
        }
        .contains(target)
  }

  private fun performInitialChecks(
      target: Long,
      numbers: List<Long>,
      operators: List<Operation>
  ): Boolean? {
    // do not perform any checks if the operations contains a concatenation
    if (operators.any { it is Operation.Concatenate }) return null

    if (target == 0L && numbers.contains(0)) return true

    //  minTarget: filter out 1 (excluded by multiplication), sum the rest
    val minTarget = numbers.filter { it != 1L }.sum()

    // maxTarget: add 1s, multiply the rest
    val maxTarget = numbers.reduce { acc, next -> if (next == 1L) acc + next else acc * next }

    if (target < minTarget || target > maxTarget) return false
    if (numbers.contains(target)) return false
    if (minTarget == target || maxTarget == target) return true

    return null
  }
}

sealed class Operation() {
  class Add : Operation()

  class Multiply : Operation()

  class Concatenate : Operation()

  fun apply(first: Long, second: Long): Long =
      when (this) {
        is Add -> first + second
        is Multiply -> first * second
        is Concatenate -> "${first}${second}".toLong()
      }
}
