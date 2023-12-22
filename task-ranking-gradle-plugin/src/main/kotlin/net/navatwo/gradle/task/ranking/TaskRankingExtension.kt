package net.navatwo.gradle.task.ranking

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class TaskRankingExtension @Inject constructor(
  objects: ObjectFactory,
) {
  val outputFileName: Property<String> = objects.property<String>().convention("task-ranking.csv")
}

