package com.dimchez.adventofcode2024.day14

import com.dimchez.adventofcode2024.DailyChallenge
import kotlin.math.absoluteValue

const val WIDTH_MAX = 101
const val WIDTH_CENTER = WIDTH_MAX / 2
const val HEIGHT_MAX = 103
const val HEIGHT_CENTER = HEIGHT_MAX / 2

class Day14(val input: List<String>) : DailyChallenge {
  override fun solveFirstChallenge(): Long =
      getRobotsFromInput()
          .map { it.getPositionAfter(100) }
          .filter { it.quadrant != Quadrant.CENTER_LINE }
          .groupingBy { it.quadrant }
          .eachCount()
          .values
          .reduce(Int::times)
          .toLong()

  override fun solveSecondChallenge(): Long {
    val robots = getRobotsFromInput()

    var adjacentRowsCount = 0
    for (i in 1..10_000) {
      val positions =
          robots
              .map { it.getPositionAfter(i) }
              .groupBy { it.x }
              .mapValues { it.value.sortedBy { it.y } }

      positions.values.forEach { locations ->
        locations.forEachIndexed { index, location ->
          locations.drop(index + 1).fold(mutableListOf(location)) { adjacentRow, next ->
            if (!next.isAdjacentHorizontallyTo(adjacentRow.last())) {
              adjacentRow.clear()
            }

            adjacentRow.add(next)

            if (adjacentRow.size == 7) {
              adjacentRowsCount++
            }
            adjacentRow
          }
        }
      }

      if (adjacentRowsCount > 10) {
        return i.toLong()
      }
    }

    return 0L
  }

  private fun getRobotsFromInput() = input.filter { it.isNotBlank() }.map { Robot.from(it) }
}

enum class Quadrant {
  TOP_LEFT,
  TOP_RIGHT,
  BOTTOM_LEFT,
  BOTTOM_RIGHT,
  CENTER_LINE
}

data class Location(val x: Int, val y: Int) {
  val quadrant =
      when {
        x < WIDTH_CENTER && y < HEIGHT_CENTER -> Quadrant.TOP_LEFT
        x > WIDTH_CENTER && y < HEIGHT_CENTER -> Quadrant.TOP_RIGHT
        x < WIDTH_CENTER && y > HEIGHT_CENTER -> Quadrant.BOTTOM_LEFT
        x > WIDTH_CENTER && y > HEIGHT_CENTER -> Quadrant.BOTTOM_RIGHT
        else -> Quadrant.CENTER_LINE
      }

  fun isAdjacentHorizontallyTo(other: Location): Boolean =
      x == other.x && (y - other.y).absoluteValue == 1
}

data class Velocity(val dx: Int, val dy: Int)

data class Robot(val location: Location, val velocity: Velocity) {
  fun getPositionAfter(seconds: Int): Location {
    return Location(
        (location.x + velocity.dx * seconds).mod(WIDTH_MAX),
        (location.y + velocity.dy * seconds).mod(HEIGHT_MAX))
  }

  companion object {
    val inputFormatRegex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

    fun from(line: String): Robot {
      val (x, y, dx, dy) =
          inputFormatRegex.find(line)?.destructured ?: error("Invalid input for Robot")
      return Robot(Location(x.toInt(), y.toInt()), Velocity(dx.toInt(), dy.toInt()))
    }
  }
}
