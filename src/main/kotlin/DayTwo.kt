import kotlin.math.abs

private const val MAX_DIFF = 3

class DayTwo(private val input: List<String>) {

  private val diffRange = 1..MAX_DIFF

  fun solveFirstChallenge(): Int {
    return checkSafeLevels(input, ::isReportSafe)
  }

  fun solveSecondChallenge(): Int {
    return checkSafeLevels(input, ::isReportSafeWithProblemDampener)
  }

  private fun checkSafeLevels(input: List<String>, isSafe: (List<Int>) -> Boolean): Int =
      input.count { line ->
        val levels = line.trim().split(whitespaceRegex).mapNotNull { it.toIntOrNull() }
        levels.isNotEmpty() && isSafe(levels)
      }

  private fun isReportSafeWithProblemDampener(levels: List<Int>): Boolean {
    if (isReportSafe(levels)) return true

    return levels.indices.any { i ->
      val modified = levels.toMutableList().apply { removeAt(i) }
      isReportSafe(modified)
    }
  }

  private fun isReportSafe(levels: List<Int>): Boolean {
    if (levels.size < 2) return false

    return checkReportSafeLevels(levels)
  }

  private fun checkReportSafeLevels(levels: List<Int>): Boolean {
    val differences = levels.zipWithNext { first, second -> second - first }

    val firstNonZeroDiff = differences.firstOrNull { it != 0 } ?: return false
    val isIncreasing = firstNonZeroDiff > 0

    return differences.all {
      it != 0 && ((isIncreasing && it > 0) || (!isIncreasing && it < 0)) && abs(it) in diffRange
    }
  }
}
