pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }

  includeBuild("build-logic")
}

rootProject.name = "task-ranking-gradle-plugin"

include(
  ":task-ranking-gradle-plugin",
)
