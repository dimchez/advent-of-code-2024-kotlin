import java.io.File
import kotlin.math.absoluteValue

class DayOne(private val filename: String) {
  fun solveFirstChallenge(): Long {
    val (leftList, rightList) = readInputLines(filename)
    return calculateTotalDistance(leftList, rightList)
  }

  fun solveSecondChallenge(): Long {
    val (leftList, rightList) = readInputLines(filename)
    val frequencyMap = buildFrequencyMap(rightList)
    return calculateSimilarityScore(leftList, frequencyMap)
  }

  private fun readInputLines(filename: String): Pair<List<Int>, List<Int>> {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()

    File(filename).forEachLine { line ->
      val parts = line.trim().split(whitespaceRegex)
      if (parts.size == 2) {
        leftList.add(parts[0].toInt())
        rightList.add(parts[1].toInt())
      }
    }

    return leftList to rightList
  }

  private fun calculateTotalDistance(left: List<Int>, right: List<Int>): Long {
    val sortedLeft = left.sorted()
    val sortedRight = right.sorted()
    return sortedLeft.zip(sortedRight).sumOf { (it.first - it.second).toLong().absoluteValue }
  }

  private fun buildFrequencyMap(numbers: List<Int>): Map<Int, Int> =
      numbers.groupingBy { it }.eachCount()

  private fun calculateSimilarityScore(leftList: List<Int>, frequencyMap: Map<Int, Int>): Long =
      leftList.fold(0L) { acc, num -> acc + num.toLong() * (frequencyMap[num] ?: 0) }
}
