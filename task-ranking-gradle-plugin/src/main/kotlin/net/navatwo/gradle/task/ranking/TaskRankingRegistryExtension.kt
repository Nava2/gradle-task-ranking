package net.navatwo.gradle.task.ranking

open class TaskRankingRegistryExtension {
  val providers: MutableSet<TaskRankingProvider> = mutableSetOf()

  companion object {
    val REGISTRY_EXTENSION_NAME = "taskRankingRegistry"
  }
}
