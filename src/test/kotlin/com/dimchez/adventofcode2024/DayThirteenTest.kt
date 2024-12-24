package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day13.DayThirteen
import com.dimchez.adventofcode2024.utils.readInputAsLines
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayThirteenTest :
    FunSpec({
      test("First challenge") {
        val dayThirteen = DayThirteen(readInputAsLines("src/test/resources/dayThirteen.txt"))
        val result = dayThirteen.solveFirstChallenge()
        result shouldBe 30973
      }

      test("Second challenge") {
        val dayThirteen = DayThirteen(readInputAsLines("src/test/resources/dayThirteen.txt"))
        val result = dayThirteen.solveSecondChallenge()
        result shouldBe 95688837203288
      }
    })
