import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayFourTest :
    FunSpec({
      test("First challenge") {
        val dayFour = DayFour(readInputAsLines("src/test/resources/dayFour.txt"))
        val result = dayFour.solveFirstChallenge()
        result shouldBe 2507
      }

      test("Second challenge") {
        val dayFour = DayFour(readInputAsLines("src/test/resources/dayFour.txt"))
        val result = dayFour.solveSecondChallenge()
        result shouldBe 1969
      }
    })
