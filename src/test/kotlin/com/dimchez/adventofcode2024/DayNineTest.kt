package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day9.DayNine
import com.dimchez.adventofcode2024.utils.readInputAsString
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayNineTest :
    FunSpec({
      test("First challenge") {
        val dayNine = DayNine(readInputAsString("src/test/resources/dayNine.txt"))
        val result = dayNine.solveFirstChallenge()
        result shouldBe 6301895872542
      }

      test("Second challenge") {
        val dayNine = DayNine(readInputAsString("src/test/resources/dayNine.txt"))
        val result = dayNine.solveSecondChallenge()
        result shouldBe 6323761685944
      }
    })
