package com.dimchez.adventofcode2024.day11

import com.dimchez.adventofcode2024.DailyChallenge

private const val FIRST_CHALLENGE_BLINK_TIMES = 25

private const val SECOND_CHALLENGE_BLINK_TIMES = 75

private const val MULTIPLIER = 2024

/** https://adventofcode.com/2024/day/11 */
class DayEleven(val input: String) : DailyChallenge {

  override fun solveFirstChallenge(): Long {
    val stones = getStones(input)
    return blink(FIRST_CHALLENGE_BLINK_TIMES, stones).size.toLong()
  }

  override fun solveSecondChallenge(): Long {
    var stones =
        getStones(input)
            .map { Stone(it) }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    val finalState = blink(SECOND_CHALLENGE_BLINK_TIMES, State(stones))
    return finalState.stones.map { it.value }.sum().toLong()
  }

  private fun getStones(input: String): List<Long> = input.trim().split(" ").map { it.toLong() }

  private tailrec fun blink(
      count: Int,
      stones: List<Long>,
  ): List<Long> {
    if (count == 0) return stones

    val newStones = mutableListOf<Long>()
    stones.forEach { stone ->
      if (stone == 0L) newStones.add(1)
      else if (hasEvenNumberOfDigits(stone)) newStones.addAll(splitNumber(stone))
      else newStones.add(stone * MULTIPLIER)
    }

    return blink(count - 1, newStones)
  }

  private tailrec fun blink(count: Int, state: State): State {
    if (count == 0) return state

    val newState =
        state.stones
            .flatMap { (stone, count) -> stone.transform().map { it to count } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }

    return blink(count - 1, State(newState))
  }

  private fun hasEvenNumberOfDigits(number: Long): Boolean = number.toString().length % 2 == 0

  private fun splitNumber(number: Long): List<Long> {
    val numberString = number.toString()
    val half = numberString.length / 2
    return listOf(numberString.substring(0, half).toLong(), numberString.substring(half).toLong())
  }
}

data class State(val stones: Map<Stone, Long>)

data class Stone(val value: Long) {
  fun transform(): Array<out Stone> =
      when {
        value == 0L -> arrayOf(Stone(1))
        value == 1L -> arrayOf(Stone(2024))
        hasEvenNumberOfDigits() -> {
          val (firstHalf, secondHalf) = splitValue()
          arrayOf(Stone(firstHalf), Stone(secondHalf))
        }
        else -> arrayOf(Stone(value * MULTIPLIER))
      }

  private fun hasEvenNumberOfDigits(): Boolean = value.toString().length % 2 == 0

  private fun splitValue(): List<Long> {
    val valueString = value.toString()
    val half = valueString.length / 2
    return listOf(valueString.substring(0, half).toLong(), valueString.substring(half).toLong())
  }
}

fun main() {
  val dayEleven = DayEleven("125 17")
  println("First challenge: ${dayEleven.solveFirstChallenge()}")
  println("Second challenge: ${dayEleven.solveSecondChallenge()}")
}
