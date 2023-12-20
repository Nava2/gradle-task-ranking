import org.gradle.kotlin.dsl.invoke

plugins {
  `maven-publish`
  signing

  kotlin("jvm")

  id("io.gitlab.arturbosch.detekt")
  id("com.jaredsburrows.license")

  id("task-ranking.versioning")
}

repositories {
  mavenCentral()

  maven {
    url = uri("https://jitpack.io")
  }
}

val javaVersion: Provider<String> = providers.fileContents(
  rootProject.layout.projectDirectory.file(".java-version"),
).asText.map { it.substringBefore('.') }

kotlin {
  jvmToolchain {
    languageVersion.set(javaVersion.map { JavaLanguageVersion.of(it) })
  }
}

tasks.test {
  useJUnitPlatform()
}

detekt {
  parallel = true
  autoCorrect = true

  buildUponDefaultConfig = true
  config.from(rootProject.file("detekt.yaml"))

  allRules = false // activate all available (even unstable) rules.
}

val libs: VersionCatalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
  detektPlugins(libs.findLibrary("detekt.formatting").get())
  detektPlugins(libs.findLibrary("faire.detekt.rules").get())
}

tasks.named("check") {
  dependsOn(tasks.named("projectHealth"))
}

licenseReport {
  generateTextReport = true
  generateHtmlReport = true
  generateCsvReport = false
  generateJsonReport = false
}

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("task-ranking-gradle-plugin")
        description.set("Gradle plugin that provides a hierarchical task ranking.")
        url.set("https://github.com/Nava2/gradle-plugin-task-ranking")

        licenses {
          license {
            name.set("MIT License")
            url.set("https://opensource.org/licenses/MIT")
          }
        }

        developers {
          developer {
            id.set("Nava2")
            name.set("Kevin Brightwell")
            email.set("kevin.brightwell2+task-ranking-gradle-plugin@gmail.com")
          }
        }

        scm {
          url.set("https://github.com/Nava2/gradle-task-ranking.git")
        }
      }
    }
  }
}

fun ProviderFactory.gradleOrSystemProperty(propertyName: String): Provider<String> {
  return gradleProperty(propertyName)
    .orElse(systemProperty(propertyName))
}

signing {
  sign(publishing.publications["maven"])
}

val projectName = project.name

tasks.withType<Jar> {
  manifest {
    val gitCommit = providers.exec {
      executable("git")
      args("rev-parse", "HEAD")
    }.standardOutput.asText.map { it.trim() }
    val gitIsDirty = providers.exec {
      executable("git")
      args("status", "--porcelain")
    }.standardOutput.asText
      .map { it.trim() }
      .map { it.isNotBlank() }

    attributes(
      "Git-Commit" to gitCommit,
      "Git-IsDirty" to gitIsDirty,
    )
  }
}

val testKitDirectory: Directory = rootProject.layout.projectDirectory.dir(".gradle/testKit")

tasks.clean {
  delete(testKitDirectory)
}

tasks.test {
  systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
  systemProperty("net.navatwo.gradle.testkit.junit5.testKitDirectory", testKitDirectory.asFile.toString())
}