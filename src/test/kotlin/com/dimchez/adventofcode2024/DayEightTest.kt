package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day8.DayEight
import com.dimchez.adventofcode2024.utils.readInputAsLines
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayEightTest :
    FunSpec({
      test("First challenge") {
        val dayEight = DayEight(readInputAsLines("src/test/resources/dayEight.txt"))
        val result = dayEight.solveFirstChallenge()
        result shouldBe 305
      }
    })
