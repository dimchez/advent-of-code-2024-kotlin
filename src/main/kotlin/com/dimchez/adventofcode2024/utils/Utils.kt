package com.dimchez.adventofcode2024.utils

import java.io.File

val whitespaceRegex = "\\s+".toRegex()

fun readInputAsLines(filename: String) = File(filename).readLines()

fun readInputAsString(filename: String) = File(filename).readText()
