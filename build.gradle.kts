plugins {
  `maven-publish`
  signing

  alias(libs.plugins.kotlin)
  alias(libs.plugins.dependencyanalysis)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)

  alias(libs.plugins.nexus.publish)

  id("task-ranking.versioning")
}

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

repositories {
  mavenCentral()
}

apiValidation {
  ignoredProjects += "task-ranking-gradle-plugin"
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

extensions.findByName("buildScan")?.withGroovyBuilder {
  setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
  setProperty("termsOfServiceAgree", "yes")
}
