package net.navatwo.gradle.task.ranking

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

open class TaskRankingGradlePlugin @Inject constructor(
  private val objects: ObjectFactory,
) : Plugin<Project> {

  override fun apply(target: Project) {
    val extension = target.extensions.create("taskRanking", TaskRankingGradlePluginExtension::class)
  }
}

