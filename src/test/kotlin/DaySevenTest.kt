import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DaySevenTest :
    FunSpec({
      test("First challenge") {
        val daySeven = DaySeven(readInputAsLines("src/test/resources/daySeven.txt"))
        val result = daySeven.solveFirstChallenge()
        result shouldBe 975671981569
      }
    })
