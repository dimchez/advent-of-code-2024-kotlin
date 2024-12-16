package com.dimchez.adventofcode2024.day8

import com.dimchez.adventofcode2024.DailyChallenge

class DayEight(private val input: List<String>) : DailyChallenge {
  override fun solveFirstChallenge(): Long =
      CityMap(input).calculateAntiNodesLocations().size.toLong()

  override fun solveSecondChallenge(): Long =
      CityMap(input).calculateAntiNodesLocationsAdjusted().size.toLong()
}

class CityMap(input: List<String>) {
  private val map: MutableList<CharArray> = input.map { it.trim().toCharArray() }.toMutableList()

  val antennas: List<Antenna> =
      map.flatMapIndexed { y, row ->
            row.mapIndexed { x, frequency ->
              if (frequency.isLetterOrDigit()) Antenna(Location(x, y), frequency) else null
            }
          }
          .filterNotNull()

  fun calculateAntiNodesLocations(): Set<Location> =
      antennas
          .groupBy { it.frequency }
          .filter { it.value.size > 1 }
          .flatMap { (_, antennas) ->
            antennas.flatMapIndexed { i, antenna1 ->
              antennas.drop(i + 1).flatMap { antenna2 ->
                listOf(antenna1.calculateAntiNode(antenna2), antenna2.calculateAntiNode(antenna1))
              }
            }
          }
          .filter(::isWithinBounds)
          .toSet()

  fun calculateAntiNodesLocationsAdjusted(): Set<Location> =
      antennas
          .groupBy { it.frequency }
          .filter { it.value.size > 1 }
          .flatMap { (_, antennas) ->
            antennas.flatMapIndexed { i, antenna1 ->
              antennas.drop(i + 1).flatMap { antenna2 ->
                val diff = antenna2.location - antenna1.location

                generateSequence(antenna2.location) { it + diff }.takeWhile { isWithinBounds(it) } +
                    generateSequence(antenna1.location) { it - diff }
                        .takeWhile { isWithinBounds(it) }
              }
            }
          }
          .toSet()

  private fun isWithinBounds(location: Location): Boolean =
      location.y in map.indices && location.x in map[0].indices
}

data class Location(val x: Int, val y: Int) {
  operator fun plus(other: Location): Location = Location(x + other.x, y + other.y)

  operator fun minus(other: Location): Location = Location(x - other.x, y - other.y)
}

fun Location.calculateAntiNode(other: Location): Location =
    Location(2 * this.x - other.x, 2 * this.y - other.y)

data class Antenna(val location: Location, val frequency: Char)

fun Antenna.calculateAntiNode(other: Antenna): Location = location.calculateAntiNode(other.location)

fun main() {
  val dayEight =
      DayEight(
          """
              ............
              ........0...
              .....0......
              .......0....
              ....0.......
              ......A.....
              ............
              ............
              ........A...
              .........A..
              ............
              ............
          """
              .trimIndent()
              .lines())
  println(dayEight.solveFirstChallenge())
  println(dayEight.solveSecondChallenge())
}
