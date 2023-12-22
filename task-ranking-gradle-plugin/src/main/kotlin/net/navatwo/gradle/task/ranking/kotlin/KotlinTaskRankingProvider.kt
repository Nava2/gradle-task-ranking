package net.navatwo.gradle.task.ranking.kotlin

import net.navatwo.gradle.task.ranking.TaskRanking
import net.navatwo.gradle.task.ranking.TaskRankingProvider
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object KotlinTaskRankingProvider : TaskRankingProvider {
  override fun appliesToTask(task: Task): Boolean {
    return task is KotlinCompile
  }

  override fun computeRanking(task: Task): TaskRanking {
    return when (task) {
      is KotlinCompile -> {
        TaskRanking(1.0)
      }

      else -> {
        error("Task ${task.name} is not supported")
      }
    }
  }
}
