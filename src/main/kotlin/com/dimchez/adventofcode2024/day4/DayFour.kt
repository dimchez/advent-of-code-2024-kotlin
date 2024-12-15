package com.dimchez.adventofcode2024.day4

import com.dimchez.adventofcode2024.DailyChallenge

private const val XMAS_SEARCH_WORD = "XMAS"

private const val MAS_SAM_SEARCH_WORD = "MAS"

class DayFour(input: List<String>) : DailyChallenge {

  private val grid: List<List<Char>> = input.map { it.trim().toList() }

  override fun solveFirstChallenge(): Long {
    return countXmasWordOccurrences(grid, XMAS_SEARCH_WORD).toLong()
  }

  override fun solveSecondChallenge(): Long {
    return countMasSamOccurrences(grid, MAS_SAM_SEARCH_WORD).toLong()
  }

  private fun countXmasWordOccurrences(valueGrid: List<List<Char>>, searchWord: String): Int {
    val rows = valueGrid.size
    val cols = valueGrid.firstOrNull()?.size ?: 0

    if (rows < searchWord.length || cols < searchWord.length) return 0

    val maxEndRow = rows - searchWord.length
    val maxEndCol = cols - searchWord.length

    val range = searchWord.indices
    val matchingLines = mutableSetOf<Int>()

    for (startRow in 0..maxEndRow) {
      for (startCol in 0..maxEndCol) {
        val lines = extractLines(valueGrid, startRow, startCol, range)
        lines
            .filter { checkLineForSearchWord(it, searchWord) }
            .mapTo(matchingLines) { it.hashCode() }
      }
    }

    return matchingLines.size
  }

  private fun countMasSamOccurrences(valueGrid: List<List<Char>>, searchWord: String): Int {
    val rows = valueGrid.size
    val cols = valueGrid.firstOrNull()?.size ?: 0

    if (rows < searchWord.length || cols < searchWord.length) return 0

    val maxEndRow = rows - searchWord.length
    val maxEndCol = cols - searchWord.length

    val range = searchWord.indices
    var count = 0
    for (startRow in 0..maxEndRow) {
      for (startCol in 0..maxEndCol) {
        val rightDownDiagonal = getRightDownDiagonal(valueGrid, startRow, startCol, range)
        val leftDownDiagonal = getLeftDownDiagonal(valueGrid, startRow, startCol, range)

        if (checkLineForSearchWord(rightDownDiagonal, searchWord) &&
            checkLineForSearchWord(leftDownDiagonal, searchWord)) {
          count++
        }
      }
    }

    return count
  }

  private fun extractLines(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange
  ): List<String> =
      extractRows(valueGrid, startRow, startCol, range) +
          extractColumns(valueGrid, startRow, startCol, range) +
          extractDiagonals(valueGrid, startRow, startCol, range)

  private fun extractRows(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange
  ): List<String> =
      range.map { r ->
        val row = startRow + r
        val rowLine = range.map { c -> valueGrid[row][startCol + c] }.joinToString("")
        "$rowLine:row=$row:col=$startCol:dx=1:dy=0"
      }

  private fun extractColumns(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange
  ): List<String> =
      range.map { c ->
        val column = startCol + c
        val colLine = range.map { r -> valueGrid[startRow + r][column] }.joinToString("")
        "$colLine:row=$startRow:col=$column:dx=0:dy=1"
      }

  private fun extractDiagonals(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange,
  ): List<String> =
      listOf(
          getRightDownDiagonal(valueGrid, startRow, startCol, range),
          getLeftDownDiagonal(valueGrid, startRow, startCol, range))

  private fun getRightDownDiagonal(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange,
  ): String =
      range
          .map { i -> valueGrid[startRow + i][startCol + i] }
          .joinToString("")
          .let { "$it:row=$startRow:col=$startCol:dx=1:dy=1" }

  private fun getLeftDownDiagonal(
      valueGrid: List<List<Char>>,
      startRow: Int,
      startCol: Int,
      range: IntRange,
  ): String =
      range
          .map { i -> valueGrid[startRow + i][startCol + (range.last - i)] }
          .joinToString("")
          .let { "$it:row=$startRow:col=$startCol:dx=-1:dy=1" }

  private fun checkLineForSearchWord(line: String, searchWord: String): Boolean =
      line.startsWith(searchWord) || line.startsWith(searchWord.reversed())
}
