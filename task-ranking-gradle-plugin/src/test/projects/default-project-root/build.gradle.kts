plugins {
  alias(libs.plugins.kotlin)
  id("navatwo.task-ranking")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(gradleTestKit())
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.assertj)

  testRuntimeOnly(libs.junit.jupiter.engine)
}

taskRanking {
  commonFixtures.from(file("src/test/projects/common"))
}