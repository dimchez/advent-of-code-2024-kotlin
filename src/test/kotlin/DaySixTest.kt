import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DaySixTest :
    FunSpec({
      test("First challenge") {
        val daySix = DaySix("src/test/resources/daySix.txt")
        val result = daySix.solveFirstChallenge()
        result shouldBe 4696
      }
    })
