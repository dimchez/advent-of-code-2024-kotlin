package com.dimchez.adventofcode2024.day10

import com.dimchez.adventofcode2024.DailyChallenge
import java.util.*

class DayTen(private val input: List<String>) : DailyChallenge {
  override fun solveFirstChallenge(): Long = GridMap(input).getTrailheadTotalScore()

  override fun solveSecondChallenge(): Long = GridMap(input).getTrailheadTotalRating()
}

data class GridMap(private val input: List<String>) {
  private val height = input.size
  private val width = input.firstOrNull()?.length ?: 0

  private val startingLocationValue = 0

  private val finalLocationValue = 9

  private val grid =
      input
          .mapNotNull { line ->
            if (line.isNotBlank()) line.map { it.toString().toInt() }.toIntArray() else null
          }
          .toTypedArray()

  fun getTrailheadTotalScore(): Long = getTrailheadsTotal { mutableSetOf() }

  fun getTrailheadTotalRating(): Long = getTrailheadsTotal { mutableListOf() }

  private fun getTrailheadsTotal(
      initializeTrailheadLocations: () -> MutableCollection<Location>
  ): Long {
    val reachedLocations =
        findTrailheads()
            .flatMap { trailhead ->
              val queue: Queue<Location> = LinkedList()
              queue.add(trailhead)
              val trailheadLocations = initializeTrailheadLocations()

              while (queue.isNotEmpty()) {
                val current = queue.remove()
                val nextLocations = getNextLocationsFrom(current)
                if (nextLocations.isEmpty() && isFinalLocation(current)) {
                  trailheadLocations.add(current)
                } else {
                  queue.addAll(nextLocations)
                }
              }

              if (trailheadLocations.isNotEmpty()) listOf(trailhead to trailheadLocations)
              else emptyList()
            }
            .toMap()

    return reachedLocations.values.sumOf { it.size }.toLong()
  }

  private fun getValueAt(location: Location): Int {
    require(isWithinBounds(location)) { "Location is out of bounds" }
    return grid[location.r][location.c]
  }

  private fun getNextLocationsFrom(location: Location): List<Location> {
    val value = getValueAt(location)
    return Direction.entries.mapNotNull { direction ->
      val nextLocation = location + direction
      if (isWithinBounds(nextLocation) && getValueAt(nextLocation) == value + 1) nextLocation
      else null
    }
  }

  private fun findTrailheads(): List<Location> =
      grid.flatMapIndexed { r, row ->
        row.mapIndexed { c, cell -> if (cell == startingLocationValue) Location(r, c) else null }
            .filterNotNull()
      }

  private fun isFinalLocation(location: Location): Boolean =
      getValueAt(location) == finalLocationValue

  private fun isWithinBounds(location: Location): Boolean =
      location.r in 0 until height && location.c in 0 until width
}

data class Location(val r: Int, val c: Int) {
  operator fun plus(direction: Direction): Location {
    return Location(r + direction.dr, c + direction.dc)
  }
}

enum class Direction(val dr: Int, val dc: Int) {
  UP(-1, 0),
  DOWN(1, 0),
  LEFT(0, -1),
  RIGHT(0, 1)
}

fun main() {
  val input =
      """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732"""
          .trimIndent()
          .lines()
  val dayTen = DayTen(input)
  println(dayTen.solveFirstChallenge())
  println(dayTen.solveSecondChallenge())
}
