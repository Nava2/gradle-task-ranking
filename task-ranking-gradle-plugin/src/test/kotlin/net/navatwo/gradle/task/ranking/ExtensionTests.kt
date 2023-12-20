package net.navatwo.gradle.task.ranking

import net.navatwo.gradle.testkit.assertj.task
import net.navatwo.gradle.testkit.junit5.GradleProject
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test

class ExtensionTests {

  @Test
  @GradleProject(projectDir = "default-project-root")
  fun `project root as Path`(
    @GradleProject.Runner gradleRunner: GradleRunner
  ) {
    val result = gradleRunner.withArguments("build").build()
    assertThat(result).task(":test").isNoSource()
  }
}
