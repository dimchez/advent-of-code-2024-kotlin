package com.dimchez.adventofcode2024

import com.dimchez.adventofcode2024.day11.DayEleven
import com.dimchez.adventofcode2024.utils.readInputAsString
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DayElevenTest :
    FunSpec({
      test("First challenge") {
        val dayEleven = DayEleven(readInputAsString("src/test/resources/dayEleven.txt"))
        val result = dayEleven.solveFirstChallenge()
        result shouldBe 239714
      }

      test("Second challenge") {
        val dayEleven = DayEleven(readInputAsString("src/test/resources/dayEleven.txt"))
        val result = dayEleven.solveSecondChallenge()
        result shouldBe 284973560658514
      }
    })
