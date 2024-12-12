import java.io.File

val whitespaceRegex = "\\s+".toRegex()

fun readInputAsLines(filename: String) = File(filename).readLines()

fun readInputAsString(filename: String) = File(filename).readText()
