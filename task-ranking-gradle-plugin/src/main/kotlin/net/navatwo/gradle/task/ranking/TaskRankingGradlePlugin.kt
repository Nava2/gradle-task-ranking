package net.navatwo.gradle.task.ranking

import net.navatwo.gradle.task.ranking.TaskRankingRegistryExtension.Companion.REGISTRY_EXTENSION_NAME
import net.navatwo.gradle.task.ranking.kotlin.KotlinTaskRankingProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.slf4j.LoggerFactory
import javax.inject.Inject

open class TaskRankingGradlePlugin @Inject constructor(
  private val objects: ObjectFactory,
) : Plugin<Project> {
  private val logger = LoggerFactory.getLogger(TaskRankingGradlePlugin::class.java)

  override fun apply(target: Project) {
    val extension = target.extensions.create("taskRanking", TaskRankingExtension::class)

    val computeRankingTask = target.tasks.register<ComputeRankingTask>(COMPUTE_RANKING_TASK_NAME)

    val taskMatchingSpec = TaskSpec(target, computeRankingTask)

    // TODO
    target.getOrCreateRegistryExtension().providers.apply {
      add(KotlinTaskRankingProvider)
    }

    target.afterEvaluate {
      target.tasks.matching(taskMatchingSpec).all { task ->
        val taskProject = task.project
        taskProject.plugins.apply(TaskRankingGradlePlugin::class.java)

        val otherTaskComputeTask = taskProject.tasks.named<ComputeRankingTask>(COMPUTE_RANKING_TASK_NAME)

        computeRankingTask.configure { computeTask ->
          computeTask.dependsOn(otherTaskComputeTask)
        }
      }
    }
  }

  private class TaskSpec(
    target: Project,
    private val computeRankingTask: TaskProvider<ComputeRankingTask>
  ) : Spec<Task> {
    val providers = target.getOrCreateRegistryExtension().providers

    override fun isSatisfiedBy(element: Task?): Boolean {
      if (element == null) {
        return false
      }

      if (element == computeRankingTask.get()) {
        return false
      }

      return providers.any { it.appliesToTask(element) }
    }
  }

  companion object {
    val COMPUTE_RANKING_TASK_NAME = "computeRanking"
  }
}

internal fun Project.getOrCreateRegistryExtension() = if (this == rootProject) {
  extensions.create<TaskRankingRegistryExtension>(REGISTRY_EXTENSION_NAME)
} else {
  rootProject.extensions.getByType<TaskRankingRegistryExtension>()
}

