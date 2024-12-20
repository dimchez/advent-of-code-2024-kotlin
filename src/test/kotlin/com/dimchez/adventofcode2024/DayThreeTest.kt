package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day3.DayThree
import com.dimchez.adventofcode2024.utils.readInputAsString
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayThreeTest :
    FunSpec({
      test("First challenge") {
        val input = readInputAsString("src/test/resources/dayThree.txt")
        val result = DayThree(input).solveFirstChallenge()
        result shouldBe 184511516
      }

      test("Second challenge") {
        val input = readInputAsString("src/test/resources/dayThree.txt")
        val result = DayThree(input).solveSecondChallenge()
        result shouldBe 90044227
      }
    })
