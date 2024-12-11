import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayOneTest :
    FunSpec({
      test("First challenge") {
        val result = DayOne("src/test/resources/dayOne.txt").solveFirstChallenge()
        result shouldBe 1830467
      }

      test("Second challenge") {
        val result = DayOne("src/test/resources/dayOne.txt").solveSecondChallenge()
        result shouldBe 26674158
      }
    })
