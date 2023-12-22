package net.navatwo.gradle.task.ranking

import org.gradle.api.Task

interface TaskRankingProvider {
  fun appliesToTask(task: Task): Boolean

  fun computeRanking(task: Task): TaskRanking
}
