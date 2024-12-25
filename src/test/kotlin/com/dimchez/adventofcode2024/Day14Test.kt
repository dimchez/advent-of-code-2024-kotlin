package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day14.Day14
import com.dimchez.adventofcode2024.utils.readInputAsLines
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day14Test :
    FunSpec({
      test("First challenge") {
        val day14 = Day14(readInputAsLines("src/test/resources/day14.txt"))
        val result = day14.solveFirstChallenge()
        result shouldBe 224554908
      }

      test("Second challenge") {
        val day14 = Day14(readInputAsLines("src/test/resources/day14.txt"))
        val result = day14.solveSecondChallenge()
        result shouldBe 6644
      }
    })
