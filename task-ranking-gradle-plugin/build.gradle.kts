plugins {
  id("task-ranking.kotlin")
  `java-gradle-plugin`
}

dependencies {
  api(libs.junit.jupiter.api)

  implementation(kotlin("reflect"))
  implementation(gradleApi())
  implementation(gradleKotlinDsl())

  compileOnly(libs.jetbrains.annotations)

  testImplementation(gradleTestKit())
  testImplementation(libs.assertj)
  testImplementation(libs.navatwo.gradle.better.testing)
  testImplementation(libs.navatwo.gradle.assertj)
  testImplementation(libs.pluginz.kotlin)

  testRuntimeOnly(libs.junit.jupiter.engine)
}

gradlePlugin {
  plugins {
    create("navatwo.task-ranking") {
      id = "navatwo.task-ranking"
      implementationClass = "net.navatwo.gradle.task.ranking.TaskRankingGradlePlugin"
    }
  }
}

