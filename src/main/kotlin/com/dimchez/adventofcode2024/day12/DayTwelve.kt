package com.dimchez.adventofcode2024.day12

import com.dimchez.adventofcode2024.DailyChallenge
import java.util.*
import kotlin.math.abs

class DayTwelve(val input: List<String>) : DailyChallenge {
  override fun solveFirstChallenge(): Long {
    return solveChallenge { region, gridMap -> region.getArea() * region.getPerimeter(gridMap) }
  }

  override fun solveSecondChallenge(): Long {
    return solveChallenge { region, gridMap -> region.getArea() * region.getSidesCount(gridMap) }
  }

  private fun solveChallenge(calculateValue: (Region, GridMap) -> Long): Long {
    val gridMap = GridMap(input)

    return gridMap.grid.foldIndexed(0L) { r, accRow, row ->
      row.foldIndexed(accRow) { c, accCell, _ ->
        val location = Location(r, c)
        if (gridMap.isVisited(location)) return@foldIndexed accCell

        val value = gridMap.getValueAt(location)
        val region = Region(value)

        val queue: Queue<Location> = LinkedList()
        queue.add(location)
        gridMap.markAsVisited(location)

        while (queue.isNotEmpty()) {
          val current = queue.remove()
          region.addCell(current)
          val nextLocations = gridMap.getMatchingValueLocationsFrom(current)
          queue.addAll(nextLocations)
        }

        accCell + calculateValue(region, gridMap)
      }
    }
  }
}

data class Location(val r: Int, val c: Int) {
  fun isAdjacent(other: Location): Boolean =
      (r == other.r && abs(c - other.c) == 1) || (c == other.c && abs(r - other.r) == 1)

  operator fun plus(direction: Direction): Location {
    return Location(r + direction.dr, c + direction.dc)
  }
}

data class Edge(val location: Location, val direction: Direction) {
  fun isAdjacent(other: Edge): Boolean = location.isAdjacent(other.location)
}

data class Region(private val value: Char) {
  private val cells = mutableSetOf<Location>()

  fun addCell(cell: Location) {
    cells.add(cell)
  }

  fun getArea() = cells.size

  fun getPerimeter(gridMap: GridMap): Long = calculateEdges(gridMap).size.toLong()

  fun getSidesCount(gridMap: GridMap): Long {
    val edges = calculateEdges(gridMap)
    val adjacentEdges = findAdjacentEdges(edges)

    return adjacentEdges.size.toLong()
  }

  private fun findAdjacentEdges(edges: List<Edge>): List<List<Edge>> {
    val groups = mutableListOf<MutableList<Edge>>()
    val visited = mutableSetOf<Edge>()

    edges.forEach { edge ->
      if (visited.contains(edge)) return@forEach

      val group = mutableListOf(edge)
      val queue: Queue<Edge> = LinkedList()
      queue.add(edge)
      visited.add(edge)

      while (queue.isNotEmpty()) {
        val current = queue.remove()
        for (other in edges) {
          if (other !in visited &&
              current.isAdjacent(other) &&
              current.direction == other.direction) {
            group.add(other)
            queue.add(other)
            visited.add(other)
          }
        }
      }

      groups.add(group)
    }

    return groups
  }

  private fun calculateEdges(gridMap: GridMap) =
      cells.flatMap { cell ->
        Direction.entries.mapNotNull { direction ->
          val nextLocation = cell + direction
          if (gridMap.isOutOfBounds(nextLocation) || gridMap.getValueAt(nextLocation) != value) {
            Edge(cell, direction)
          } else null
        }
      }
}

data class GridMap(private val input: List<String>) {
  val height = input.size

  val width = input.firstOrNull()?.length ?: 0

  val grid =
      input
          .mapNotNull { line ->
            if (line.isNotBlank()) line.map { it.toChar() }.toCharArray() else null
          }
          .toTypedArray()

  val visited = Array(height) { BooleanArray(width) }

  fun getNextLocationsFrom(location: Location): List<Location> =
      Direction.entries.mapNotNull { direction ->
        val nextLocation = location + direction
        if (isWithinBounds(nextLocation)) nextLocation else null
      }

  fun getMatchingValueLocationsFrom(location: Location): List<Location> {
    val value = getValueAt(location)
    val nextLocations = getNextLocationsFrom(location)

    return nextLocations.mapNotNull { location ->
      if (getValueAt(location) == value && !isVisited(location)) {
        markAsVisited(location)
        location
      } else {
        null
      }
    }
  }

  fun markAsVisited(location: Location) {
    check(isWithinBounds(location)) { "Location is out of bounds" }
    visited[location.r][location.c] = true
  }

  fun getValueAt(location: Location): Char {
    check(isWithinBounds(location)) { "Location is out of bounds" }
    return grid[location.r][location.c]
  }

  fun isVisited(location: Location): Boolean {
    check(isWithinBounds(location)) { "Location is out of bounds" }
    return visited[location.r][location.c]
  }

  fun isWithinBounds(location: Location): Boolean =
      location.r >= 0 && location.r < height && location.c >= 0 && location.c < width

  fun isOutOfBounds(location: Location): Boolean = !isWithinBounds(location)
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
        AAAA
        BBCD
        BBCC
        EEEC
    """
          .trimIndent()

  val dayTwelve = DayTwelve(input.lines())
  println(dayTwelve.solveFirstChallenge())
  println(dayTwelve.solveSecondChallenge())
}
