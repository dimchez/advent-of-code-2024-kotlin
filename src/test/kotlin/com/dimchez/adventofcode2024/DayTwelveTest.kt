package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day12.DayTwelve
import com.dimchez.adventofcode2024.utils.readInputAsLines
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayTwelveTest :
    FunSpec({
      test("First challenge") {
        val day12 = DayTwelve(readInputAsLines("src/test/resources/dayTwelve.txt"))
        val result = day12.solveFirstChallenge()
        result shouldBe 1550156
      }

      test("Second challenge") {
        val day12 = DayTwelve(readInputAsLines("src/test/resources/dayTwelve.txt"))
        val result = day12.solveSecondChallenge()
        result shouldBe 946084
      }
    })
