package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day5.DayFive
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayFiveTest :
    FunSpec({
      test("First Challenge") {
        val dayFive = DayFive("src/test/resources/dayFive.txt")
        val result = dayFive.solveFirstChallenge()

        result shouldBe 4790
      }

      test("Second Challenge") {
        val dayFive = DayFive("src/test/resources/dayFive.txt")
        val result = dayFive.solveSecondChallenge()

        result shouldBe 6319
      }
    })
