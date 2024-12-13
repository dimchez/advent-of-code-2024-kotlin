class DaySix(private val filename: String) {
  fun solveFirstChallenge(): Int {
    val input = readInputAsString(filename)
    val startLocation = getStartingLocation(input)
    val map = GameFieldMap(input, startLocation)
    val guard = Guard(startLocation)
    var nextLocation = startLocation + guard.direction

    while (map.isWithinBounds(nextLocation)) {
      if (map.isEmpty(nextLocation)) {
        guard.moveForward()
      } else {
        guard.turnRight()
      }

      nextLocation = guard.location + guard.direction
    }

    return guard.countVisited()
  }

  private fun getStartingLocation(input: String): Location {
    val index = input.indexOfFirst { it == '^' }

    require(index != -1) { "No starting location found" }

    val matrix = input.lines().map { it.toCharArray() }

    val startX = index / matrix.size
    val startY = index % matrix.size

    return Location(startX, startY)
  }
}

class GameFieldMap(input: String, startLocation: Location) {
  private val map =
      input.lines().map { it.toCharArray() }.apply { this[startLocation.x][startLocation.y] = '.' }

  fun isEmpty(location: Location): Boolean = map[location.x][location.y] == '.'

  fun isWithinBounds(location: Location): Boolean =
      location.x in map.indices && location.y in map[0].indices
}

class Guard(startingPosition: Location) {
  var location = startingPosition
    private set

  var direction = DirectionEnum.UP
    private set

  private val visited = mutableSetOf(startingPosition)

  fun turnRight() {
    direction = direction.turnRight()
  }

  fun moveForward() {
    location += direction
    visited.add(location)
  }

  fun countVisited(): Int = visited.size
}

data class Location(val x: Int, val y: Int) {
  operator fun plus(other: DirectionEnum): Location = Location(x + other.x, y + other.y)
}

enum class DirectionEnum(val x: Int, val y: Int) {
  RIGHT(0, 1),
  DOWN(1, 0),
  LEFT(0, -1),
  UP(-1, 0);

  fun turnRight(): DirectionEnum =
      when (this) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        UP -> RIGHT
      }
}
