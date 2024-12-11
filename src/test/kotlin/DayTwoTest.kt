import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayTwoTest :
    FunSpec({
      test("First challenge") {
        val input = readInputAsLines("src/test/resources/dayTwo.txt")
        val result = DayTwo(input).solveFirstChallenge()
        result shouldBe 490
      }

      test("Second challenge") {
        val input = readInputAsLines("src/test/resources/dayTwo.txt")
        val result = DayTwo(input).solveSecondChallenge()
        result shouldBe 536
      }
    })
