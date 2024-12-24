package com.dimchez.adventofcode2024.day13

import com.dimchez.adventofcode2024.DailyChallenge

class DayThirteen(val input: List<String>) : DailyChallenge {

  private val buttonValuesRegex = "X\\+(\\d+),.*Y\\+(\\d+)$".toRegex()

  private val prizeValuesRegex = "X=(\\d+),.*Y=(\\d+)$".toRegex()

  override fun solveFirstChallenge(): Long = getGamesFromInput().sumOf { it.calculateCost() }

  override fun solveSecondChallenge(): Long =
      getGamesFromInput()
          .map {
            it.copy(
                it.buttonA,
                it.buttonB,
                Prize(it.prize.x + 10_000_000_000_000, it.prize.y + 10_000_000_000_000))
          }
          .sumOf { it.calculateCost() }

  private fun getGamesFromInput() =
      input.chunked(4).map { lines ->
        val buttonA =
            buttonValuesRegex.find(lines[0])?.destructured?.let { (x, y) ->
              Button.A(x.toInt(), y.toInt())
            } ?: error("Invalid input for Button A")
        val buttonB =
            buttonValuesRegex.find(lines[1])?.destructured?.let { (x, y) ->
              Button.B(x.toInt(), y.toInt())
            } ?: error("Invalid input for Button B")
        val prize =
            prizeValuesRegex.find(lines[2])?.destructured?.let { (x, y) ->
              Prize(x.toLong(), y.toLong())
            } ?: error("Invalid input for Prize")
        Game(buttonA, buttonB, prize)
      }
}

sealed class Button(val x: Int, val y: Int, val cost: Int) {
  class A(x: Int, y: Int) : Button(x, y, 3)

  class B(x: Int, y: Int) : Button(x, y, 1)
}

data class Prize(val x: Long, val y: Long)

data class Game(val buttonA: Button.A, val buttonB: Button.B, val prize: Prize) {
  fun calculateCost(): Long {
    val determinant = buttonA.x * buttonB.y - buttonA.y * buttonB.x
    check(determinant != 0) { "Determinant is 0" }

    val x = (prize.x * buttonB.y - prize.y * buttonB.x) / determinant
    val y = (prize.y * buttonA.x - prize.x * buttonA.y) / determinant

    return if (x * buttonA.x + y * buttonB.x == prize.x &&
        x * buttonA.y + y * buttonB.y == prize.y) {
      buttonA.cost * x + buttonB.cost * y
    } else 0
  }
}

fun main() {
  val input =
      """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
        
    """
          .trimIndent()

  val day13 = DayThirteen(input.lines())
  println(day13.solveFirstChallenge())
}
