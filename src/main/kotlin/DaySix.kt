const val EMPTY_SPACE = '.'

const val OBSTRUCTION = '#'

const val GUARD = '^'

class DaySix(private val filename: String) {
  fun solveFirstChallenge(): Int {
    val input = readInputAsString(filename)
    val map = GameFieldMap(input)
    val guard = Guard(map.startLocation)

    moveGuard(guard, map)

    return guard.visited.size
  }

  fun solveSecondChallenge(): Int {
    val input = readInputAsString(filename)
    val map = GameFieldMap(input)
    val guard = Guard(map.startLocation)

    moveGuard(guard, map)
    val visited = guard.visited.filter { it != map.startLocation }

    return visited
        .filter { it != map.startLocation }
        .count { location ->
          map.placeObstacle(location)
          val newGuard = Guard(map.startLocation)
          moveGuard(newGuard, map)
          map.removeObstacle(location)

          newGuard.isRepeatedMove()
        }
  }

  private fun moveGuard(guard: Guard, map: GameFieldMap) {
    var nextLocation = guard.location + guard.direction

    while (map.isWithinBounds(nextLocation) && !guard.isRepeatedMove()) {
      if (map.isEmpty(nextLocation)) {
        guard.moveForward()
      } else {
        guard.turnRight()
      }

      nextLocation = guard.location + guard.direction
    }
  }
}

class GameFieldMap(input: String) {
  private val map: MutableList<CharArray> =
      input.trimEnd().lines().map { it.toCharArray() }.toMutableList()

  val startLocation: Location

  init {
    val line = map.find { it.contains(GUARD) } ?: error("No starting location found")
    val startX = map.indexOf(line)
    val startY = line.indexOf(GUARD)

    this.startLocation = Location(startX, startY)

    map[startLocation.x][startLocation.y] = EMPTY_SPACE
  }

  fun isEmpty(location: Location): Boolean {
    require(isWithinBounds(location)) { "Location is out of bounds" }
    return map[location.x][location.y] == EMPTY_SPACE
  }

  fun isWithinBounds(location: Location): Boolean =
      location.x in map.indices && location.y in map[0].indices

  fun placeObstacle(location: Location) {
    require(isWithinBounds(location)) { "Location is out of bounds" }
    map[location.x][location.y] = OBSTRUCTION
  }

  fun removeObstacle(location: Location) {
    require(isWithinBounds(location)) { "Location is out of bounds" }
    map[location.x][location.y] = EMPTY_SPACE
  }
}

class Guard(startingPosition: Location) {
  var location = startingPosition
    internal set

  var direction = Direction.UP
    private set

  val visited = mutableSetOf(startingPosition)

  val moves = mutableSetOf<Pair<Location, Direction>>()

  fun turnRight() {
    direction = direction.turnRight()
  }

  fun moveForward() {
    moves.add(location to direction)
    location += direction
    visited.add(location)
  }

  fun isRepeatedMove(): Boolean = moves.contains(location to direction)
}

data class Location(val x: Int, val y: Int) {
  operator fun plus(other: Direction): Location = Location(x + other.x, y + other.y)
}

enum class Direction(val x: Int, val y: Int) {
  RIGHT(0, 1),
  DOWN(1, 0),
  LEFT(0, -1),
  UP(-1, 0);

  fun turnRight(): Direction =
      when (this) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        UP -> RIGHT
      }
}
