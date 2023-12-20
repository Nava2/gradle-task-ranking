package net.navatwo.gradle.task.ranking

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.slf4j.LoggerFactory
import javax.inject.Inject

open class TaskRankingGradlePlugin @Inject constructor() : Plugin<Project> {
  private val logger = LoggerFactory.getLogger(TaskRankingGradlePlugin::class.java)

  override fun apply(target: Project) {
    val extension = target.extensions.create("taskRanking", TaskRankingGradlePluginExtension::class)
    logger.info("TaskRankingGradlePlugin applied to ${target.name}")
    logger.info("Extension: $extension")
  }
}
