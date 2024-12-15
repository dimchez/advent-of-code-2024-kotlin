package com.dimchez.adventofcode2024.day8

import com.dimchez.adventofcode2024.DailyChallenge

class DayEight(private val input: List<String>) : DailyChallenge {
  override fun solveFirstChallenge(): Long {
    val cityMap = CityMap(input)
    val antiNodesLocations = cityMap.calculateAntiNodesLocations()

    return antiNodesLocations.size.toLong()
  }

  override fun solveSecondChallenge(): Long {
    throw NotImplementedError()
  }
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

  private fun isWithinBounds(location: Location): Boolean =
      location.y in map.indices && location.x in map[0].indices
}

data class Location(val x: Int, val y: Int)

fun Location.calculateAntiNode(other: Location): Location {
  return Location(2 * this.x - other.x, 2 * this.y - other.y)
}

data class Antenna(val location: Location, val frequency: Char)

fun Antenna.calculateAntiNode(other: Antenna): Location {
  return location.calculateAntiNode(other.location)
}

fun main() {
  val dayEight =
      DayEight(
          listOf(
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
                  .trimIndent()))
  println(dayEight.solveFirstChallenge())
}
