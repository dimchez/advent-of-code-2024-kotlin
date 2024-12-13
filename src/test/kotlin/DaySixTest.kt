import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.system.measureTimeMillis

class DaySixTest :
    FunSpec({
      test("First challenge") {
        val daySix = DaySix("src/test/resources/daySix.txt")
        val result = daySix.solveFirstChallenge()
        result shouldBe 4696
      }

      test("Second challenge") {
        val daySix = DaySix("src/test/resources/daySix.txt")
        val executionTime = measureTimeMillis {
          val result = daySix.solveSecondChallenge()
          result shouldBe 1443
        }
        println("Execution time: $executionTime ms")
      }
    })
