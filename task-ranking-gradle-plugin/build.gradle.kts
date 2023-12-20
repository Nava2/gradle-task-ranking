plugins {
  id("task-ranking.kotlin")
  `java-gradle-plugin`
}

dependencies {
  implementation(gradleApi())
  implementation(gradleKotlinDsl())

  compileOnly(libs.jetbrains.annotations)

  testImplementation(gradleTestKit())
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.assertj)
  testImplementation(libs.navatwo.gradle.better.testing)
  testImplementation(libs.navatwo.gradle.assertj)

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

