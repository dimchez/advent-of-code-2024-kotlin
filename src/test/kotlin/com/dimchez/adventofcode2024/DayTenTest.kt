package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day10.DayTen
import com.dimchez.adventofcode2024.utils.readInputAsLines
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayTenTest :
    FunSpec({
      test("First challenge") {
        val dayTen = DayTen(readInputAsLines("src/test/resources/dayTen.txt"))
        val result = dayTen.solveFirstChallenge()
        result shouldBe 607
      }

      test("Second challenge") {
        val dayTen = DayTen(readInputAsLines("src/test/resources/dayTen.txt"))
        val result = dayTen.solveSecondChallenge()
        result shouldBe 1384
      }
    })
