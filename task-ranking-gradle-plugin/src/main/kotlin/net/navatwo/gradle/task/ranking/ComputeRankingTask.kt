package net.navatwo.gradle.task.ranking

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.setProperty
import javax.inject.Inject

open class ComputeRankingTask @Inject constructor(
  objects: ObjectFactory,
) : DefaultTask() {
  private val projectExtension = project.extensions.getByType<TaskRankingExtension>()
  private val registryExtension = project.getOrCreateRegistryExtension()


  init {
    group = "reporting"
    description = "Computes the ranking of tasks in a project."

    notCompatibleWithConfigurationCache("This walks task dependencies, it is not compatible.")
  }

  @get:Internal
  val dependentRankings = objects.mapProperty<Project, RegularFile>()

  @InputFiles
  @PathSensitive(PathSensitivity.RELATIVE)
  val dependentRankingOutputs = objects.fileCollection()
    .from(
      dependentRankings.keySet().map { projects ->
        projects.map { dependentRankings.getting(it) }
      }
    )

  @Input
  val taskPaths = objects.setProperty<String>()

  @OutputFile
  val computedFile: Property<RegularFile> = objects.fileProperty()
    .value(
      project.layout.buildDirectory.dir("generated-reports/task-ranking")
        .zip(projectExtension.outputFileName) { dir, file -> dir.file(file) }
    )

  @TaskAction
  fun compute() {
    val dependentTaskRankings = dependentRankings.get().entries
      .flatMap { (_, file) ->
        file.asFile.useLines { lines ->
          lines.map { line ->
            val (taskPath, ranking) = line.split(",", limit = 2)
            taskPath to TaskRanking(ranking.toDouble())
          }
            .toList()
        }
      }
      .toMap()

    val taskRankings = project.tasks.map { task ->

    }
  }
}