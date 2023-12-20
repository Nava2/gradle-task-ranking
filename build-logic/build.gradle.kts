plugins {
  kotlin("jvm") version "1.9.21"
  `kotlin-dsl`
}

repositories {
  mavenCentral()
}

val kotlinVersion = "1.9.10"
val detektVersion = "1.23.3"

dependencies {
  implementation(libs.pluginz.kotlin)

  implementation(libs.pluginz.detekt)

  implementation(libs.pluginz.license)
}

val javaVersion = providers.fileContents(rootProject.layout.projectDirectory.file(".java-version"))

kotlin {
  jvmToolchain {
    languageVersion.set(javaVersion.asText.map { JavaLanguageVersion.of(it) })
  }
}
