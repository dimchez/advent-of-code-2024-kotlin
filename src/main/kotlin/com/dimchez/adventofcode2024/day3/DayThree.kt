package com.dimchez.adventofcode2024.day3

import com.dimchez.adventofcode2024.DailyChallenge

class DayThree(private val input: String) : DailyChallenge {

  private val regexTotal = """mul\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\)""".toRegex()

  private val regexTotalWithConditions =
      """(mul\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\)|do\(\s*\)|don't\(\s*\))""".toRegex()

  override fun solveFirstChallenge(): Long {
    return calculateTotal(input)
  }

  override fun solveSecondChallenge(): Long {
    return calculateTotalWithConditions(input)
  }

  private fun calculateTotal(input: String): Long =
      regexTotal.findAll(input).sumOf { matchResult ->
        val (xStr, yStr) = matchResult.destructured
        xStr.toLong() * yStr.toLong()
      }

  private fun calculateTotalWithConditions(input: String): Long =
      regexTotalWithConditions
          .findAll(input)
          .fold(Pair(0L, true)) { acc, matchResult ->
            val (totalSum, mulEnabled) = acc
            when {
              matchResult.value.startsWith("mul(") && mulEnabled -> {
                val (_, xStr, yStr) = matchResult.destructured
                totalSum + xStr.toLong() * yStr.toLong() to true
              }
              matchResult.value.startsWith("do(") -> totalSum to true
              matchResult.value.startsWith("don't(") -> totalSum to false
              else -> totalSum to mulEnabled
            }
          }
          .first
}
