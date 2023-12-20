package net.navatwo.gradle.task.ranking

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class TaskRankingGradlePluginExtension @Inject constructor(
  objects: ObjectFactory,
) {
  /**
   * Defines the location for common fixtures to populate for each project.
   */
  val commonFixtures: ConfigurableFileCollection = objects.fileCollection()
}