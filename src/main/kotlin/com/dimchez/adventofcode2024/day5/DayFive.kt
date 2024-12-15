package com.dimchez.adventofcode2024.day5

import com.dimchez.adventofcode2024.DailyChallenge
import java.io.FileReader
import java.util.*

class DayFive(private val filename: String) : DailyChallenge {
  override fun solveFirstChallenge(): Long {
    val (rules, updates) = readRulesAndUpdates()

    return updates
        .fold(0) { acc, update ->
          val pages = getPages(update)
          val pagesIndexed = getPagesIndexed(pages)

          val matchingRules = getMatchingRules(rules, pagesIndexed)
          val isCorrectOrder = checkIsCorrectOrder(matchingRules, pagesIndexed)

          if (isCorrectOrder) {
            acc + pages[pages.size / 2]
          } else {
            acc
          }
        }
        .toLong()
  }

  override fun solveSecondChallenge(): Long {
    val (rules, updates) = readRulesAndUpdates()

    return updates
        .fold(0) { acc, update ->
          val pages = getPages(update)
          val pagesIndexed = getPagesIndexed(pages)

          val matchingRules = getMatchingRules(rules, pagesIndexed)
          val isCorrectOrder = checkIsCorrectOrder(matchingRules, pagesIndexed)

          if (isCorrectOrder) {
            acc
          } else {
            val correctedOrder = topologicalSortSubset(pages, matchingRules)
            acc + correctedOrder[correctedOrder.size / 2]
          }
        }
        .toLong()
  }

  private fun checkIsCorrectOrder(
      matchingRules: List<Pair<Int, Int>>,
      pagesIndexed: Map<Int, Int>
  ) = matchingRules.all { pagesIndexed[it.first]!! < pagesIndexed[it.second]!! }

  private fun getMatchingRules(rules: List<Pair<Int, Int>>, pagesIndexed: Map<Int, Int>) =
      rules.filter { it.first in pagesIndexed && it.second in pagesIndexed }

  private fun getPages(update: String): List<Int> = update.split(",").map { it.trim().toInt() }

  private fun getPagesIndexed(pages: List<Int>): Map<Int, Int> =
      pages.withIndex().associate { it.value to it.index }

  private fun readRulesAndUpdates(): Pair<List<Pair<Int, Int>>, List<String>> {
    val rules = mutableListOf<Pair<Int, Int>>()
    val updates = mutableListOf<String>()
    var readRules = true

    FileReader(filename).forEachLine { line ->
      if (line.isEmpty() || line.isBlank()) {
        readRules = false
      } else if (readRules) {
        val (first, second) = line.split("|").map { it.toInt() }
        rules.add(first to second)
      } else {
        updates.add(line)
      }
    }

    return rules to updates
  }

  /**
   * Perform a topological sort using Kahn's algorithm on the subset of pages based on applicable
   * rules.
   * https://mohammad-imran.medium.com/understanding-topological-sorting-with-kahns-algo-8af5a588dd0e
   */
  private fun topologicalSortSubset(
      pages: List<Int>,
      applicableRules: List<Pair<Int, Int>>
  ): List<Int> {
    val pageSet = pages.toSet()

    val (adjacency, inDegree) = initGraph(pageSet, applicableRules)

    val sortedOrder = sortPageSet(pageSet, inDegree, adjacency)

    // sortedOrder should contain all pages if no cycles exist
    check(sortedOrder.size == pageSet.size, { "Cycle detected in topological sort" })

    return sortedOrder
  }

  private fun initGraph(
      pageSet: Set<Int>,
      applicableRules: List<Pair<Int, Int>>
  ): Pair<MutableMap<Int, MutableList<Int>>, MutableMap<Int, Int>> {
    // Adjacency list and in-degree count (number of incoming edges) for each node
    val adjacency =
        mutableMapOf<Int, MutableList<Int>>().apply { pageSet.forEach { put(it, mutableListOf()) } }
    val inDegree =
        mutableMapOf<Int, Int>().withDefault { 0 }.apply { pageSet.forEach { put(it, 0) } }

    // Fill edges, keep track of number of incoming edges for each node
    applicableRules.map { (x, y) ->
      adjacency[x]!!.add(y)
      inDegree[y] = inDegree.getValue(y) + 1
    }

    return Pair(adjacency, inDegree)
  }

  // Execute Kahn's algorithm for topological sort
  private fun sortPageSet(
      pageSet: Set<Int>,
      inDegree: MutableMap<Int, Int>,
      adjacency: MutableMap<Int, MutableList<Int>>
  ): List<Int> {
    // Start with adding all nodes with in-degree 0 to the queue
    val queue =
        LinkedList<Int>().apply {
          pageSet.filter { inDegree.getValue(it) == 0 }.forEach { add(it) }
        }

    // Remove node from the queue, decrement in-degree of neighbors
    // Add node to the sorted order when all incoming edges are removed
    return generateSequence { if (queue.isNotEmpty()) queue.removeFirst() else null }
        .onEach { node ->
          adjacency[node]!!.forEach { neighbor ->
            inDegree[neighbor] = inDegree.getValue(neighbor) - 1
            if (inDegree.getValue(neighbor) == 0) {
              queue.add(neighbor)
            }
          }
        }
        .toList()
  }
}
