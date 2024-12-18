package com.dimchez.adventofcode2024.day9

import com.dimchez.adventofcode2024.DailyChallenge

class DayNine(val input: String) : DailyChallenge {
  override fun solveFirstChallenge(): Long {
    val segments = parseFileSegments()
    val diskImage = DiskImageCompact(segments)
    diskImage.compact()

    return diskImage.calculateChecksum()
  }

  override fun solveSecondChallenge(): Long {
    val segments = parseFileSegments()
    val diskImage = DiskImageFragmented(segments)
    diskImage.compact()

    return diskImage.calculateChecksum()
  }

  private fun parseFileSegments(): List<FileSegment> =
      input
          .map { it.toString().toInt() }
          .chunked(2)
          .map { FileSegment(it.first(), it.elementAtOrNull(1) ?: 0) }
}

data class FileSegment(val fileLength: Int, val freeSpace: Int)

interface DiskFragment {
  val id: Int

  fun push(fileId: Int)

  fun pop(): Int

  fun getAll(): List<Int?>

  fun hasFreeSpace(): Boolean

  fun isEmpty(): Boolean

  fun isNotEmpty(): Boolean
}

data class DiskFragmentLinear(override val id: Int, private val segment: FileSegment) :
    DiskFragment {
  private val length = segment.fileLength + segment.freeSpace

  private val files = arrayOfNulls<Int>(length)

  private var lastFileIndex = -1

  override fun push(fileId: Int) {
    check(hasFreeSpace()) { "DiskFragment is full" }

    lastFileIndex++
    files[lastFileIndex] = fileId
  }

  override fun pop(): Int {
    check(isNotEmpty()) { "DiskFragment is empty" }

    val fileId = files[lastFileIndex]
    checkNotNull(fileId) { "DiskFragment is empty" }

    files[lastFileIndex] = null
    lastFileIndex--
    return fileId
  }

  override fun getAll(): List<Int?> = files.toList()

  override fun hasFreeSpace(): Boolean = lastFileIndex < files.lastIndex

  override fun isEmpty(): Boolean = lastFileIndex == -1

  override fun isNotEmpty(): Boolean = lastFileIndex != -1
}

data class DiskFragmentSplit(override val id: Int, private val segment: FileSegment) :
    DiskFragment {
  private val usedSpace = arrayOfNulls<Int>(segment.fileLength).apply { fill(id) }

  private val freeSpace = arrayOfNulls<Int>(segment.freeSpace)

  val free: Int
    get() = freeSpace.filter { it == null }.size

  val used: Int
    get() = usedSpace.filter { it == id }.size

  override fun push(fileId: Int) {
    check(hasFreeSpace()) { "No free space available" }
    freeSpace.indexOfFirst { it == null }.let { index -> freeSpace[index] = fileId }
  }

  override fun pop(): Int {
    check(isNotEmpty()) { "DiskFragment is empty" }
    return usedSpace
        .indexOfLast { it == id }
        .let { index ->
          usedSpace[index] = null
          id
        }
  }

  override fun getAll(): List<Int?> = usedSpace.toList() + freeSpace.toList()

  override fun hasFreeSpace(): Boolean = freeSpace.contains(null)

  override fun isEmpty(): Boolean = !usedSpace.contains(id)

  override fun isNotEmpty(): Boolean = usedSpace.contains(id)
}

interface DiskImage {
  val diskFragments: List<DiskFragment>

  fun compact()

  fun calculateChecksum(): Long =
      diskFragments.flatMap(DiskFragment::getAll).foldIndexed(0L) { index, acc, fileId ->
        fileId?.toLong()?.let { acc + it * index } ?: acc
      }
}

data class DiskImageCompact(private val segments: List<FileSegment>) : DiskImage {
  override val diskFragments: List<DiskFragment> =
      segments.mapIndexed { i, segment ->
        DiskFragmentLinear(i, segment).apply { repeat(segment.fileLength) { push(i) } }
      }

  override fun compact() {
    var start = 0
    var end = diskFragments.size - 1

    while (start < end) {
      val startFragment = diskFragments[start]
      val endFragment = diskFragments[end]

      while (startFragment.hasFreeSpace() && endFragment.isNotEmpty()) {
        startFragment.push(endFragment.pop())
      }

      if (!startFragment.hasFreeSpace()) {
        start++
      }
      if (endFragment.isEmpty()) {
        end--
      }
    }
  }
}

data class DiskImageFragmented(private val segments: List<FileSegment>) : DiskImage {
  override val diskFragments: List<DiskFragmentSplit> =
      segments.mapIndexed { i, segment -> DiskFragmentSplit(i, segment) }

  override fun compact() {
    for (i in diskFragments.size - 1 downTo 1) {
      val fragment = diskFragments[i]

      diskFragments
          .find { it.free >= fragment.used && it.id < fragment.id }
          ?.let { otherFragment ->
            while (fragment.isNotEmpty()) {
              otherFragment.push(fragment.pop())
            }
          }
    }
  }
}

fun main() {
  val dayNine = DayNine("2333133121414131402")
  println(dayNine.solveFirstChallenge())
  println(dayNine.solveSecondChallenge())
}
