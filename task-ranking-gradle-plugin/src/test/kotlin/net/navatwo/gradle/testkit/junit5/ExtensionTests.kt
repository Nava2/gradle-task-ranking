package net.navatwo.gradle.testkit.junit5

import net.navatwo.gradle.testkit.assertj.task
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import java.nio.file.Path

class ExtensionTests {

  @Test
  @GradleProject(projectDir = "default-project-root")
  fun `project root as Path`(
    @GradleProject.Root projectRoot: Path,
    @GradleProject.Runner gradleRunner: GradleRunner
  ) {
    val result = gradleRunner.withDebug(true).withArguments("build").build()
    assertThat(result).task(":test").isNoSource()
  }
}
