# gradle-plugin-task-ranking
A collection of libraries to make testing gradle plugins in Kotlin better.

## JUnit 5 Extensions

Provides a JUnit 5 extension for loading Gradle Projects in a consistent way within tests.


### Full example

```gradle
dependencies {
  testImplementation("net.navatwo:gradle-plugin-task-ranking-junit5:0.0.5")
}

tasks.test {
  // Enable auto-detection of JUnit extensions, avoids adding `@ExtendsWith(...)` to every test.
  systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")

  // Configure the gradle version from a gradle property, e.g. when running a test matrix across multiple gradle 
  // versions 
  // To use an environment variable, switch `gradleProperty` with `environmentVariable`.
  val gradleVersion = providers.gradleProperty("test.gradleVersion")
  if (gradleVersion.isPresent) {
     systemProperty("net.navatwo.gradle.testkit.junit5.gradleVersion", gradleVersion.get())
  }

  // Use a shared directory across all projects 
  val testKitDirectory: Directory = rootProject.layout.projectDirectory.dir(".gradle/testKit")
  systemProperty("net.navatwo.gradle.testkit.junit5.testKitDirectory", testKitDirectory.asFile.toString())
}
```

### Add to your project
```gradle
dependencies {
  testImplementation("net.navatwo:gradle-plugin-task-ranking-junit5:0.0.5")
}
```

This provides sensible defaults for performance and ease of use. All defaults are overridden via annotations. To use
this extension, enable extensions in your project either via:
1. **(Recommended)** Add `junit.jupiter.extensions.autodetection.enabled` as `true` to your Test JVM arguments, e.g.:
     ```gradle
     tasks.test {
       systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
     }
     ```
2. Add `@ExtendsWith(GradleTestKitProjectExtension::class)` to your test class

This extension assumes projects are defined in a "projects" directory (e.g. `src/test/projects`). This can be
overridden by annotating your test class with [`GradleProjectsRoot`](gradle-plugin-task-ranking-junit5/src/main/kotlin/net/navatwo/gradle/testkit/junit5/GradleProjectsRoot.kt)
(e.g. `@GradleProjectsRoot("src/test/other-projects")`).

To use a specific project, annotate a test method with [`GradleProject`](gradle-plugin-task-ranking-junit5/src/main/kotlin/net/navatwo/gradle/testkit/junit5/GradleProject.kt):

```kotlin
@Test
@GradleProject("lazy-evaluation-successful")
fun `lazy evaluation is successful`(@GradleProject.Runner gradleRunner: GradleRunner) {
  assertThat(gradleRunner.withArguments("tasks").build()).task(":tasks").isSuccess()
}
```

Annotating with `@GradleProject.Runner` allows injecting a pre-configured [`GradleRunner`](https://docs.gradle.org/current/javadoc/org/gradle/testkit/runner/GradleRunner.html).

### Configuration

Test suite and method level configurations are done via the
[`GradleTestKitConfiguration`](gradle-plugin-task-ranking-junit5/src/main/kotlin/net/navatwo/gradle/testkit/junit5/GradleTestKitConfiguration.kt)
annotation. Any values specified in these annotations will be used to override default values. The value specified 
"closest" to the test method will always take precedence.

### Manipulating projects in `@BeforeEach`

If looking to have common setup within a test class, annotating a method with `@BeforeEach` and passing the parameter
`@GradleProject.Runner` or `@GradleProject.Root` allows for shared configuration. For example, see 
[`BeforeEachParameterInjectionTest.kt`](gradle-plugin-task-ranking-junit5/src/test/kotlin/net/navatwo/gradle/testkit/junit5/integration_test/BeforeEachParameterInjectionTest.kt).

### Gradle TestKit 

By default, this extension will set any injected `GradleRunner` to share a `TestKit` directory in the `build/`
directory for the project - `${project_dir}/build/test-kit`. This can be overridden by annotating your test class with
[`GradleTestKitConfiguration`](gradle-plugin-task-ranking-junit5/src/main/kotlin/net/navatwo/gradle/testkit/junit5/GradleTestKitConfiguration.kt).
This is done to _greatly_ improve the speed of tests by avoiding re-downloading Gradle
dependencies with each test run.

We recommend to share the test kit directory across all projects to avoid needless downloading. For example:
```kotlin
val testKitDirectory: Directory = rootProject.layout.projectDirectory.dir(".gradle/testKit")

tasks.test {
   systemProperty("net.navatwo.gradle.testkit.junit5.testKitDirectory", testKitDirectory.asFile.toString())
}
```

## Development

### Releasing

Setup your local `~/.gradle/gradle.properties` with the following variables:

```
signing.keyId=<last eight digits of key id>
signing.password=<password>
signing.secretKeyRingFile=/Users/my_user/.gnupg/secring.gpg

sonatypeUsername=<sonatype user token>
sonatypePassword=<sonatype user token password>
```

```shell
# Clean the repo first to not have any old artifacts
./gradlew clean

# Verify the repo is in good shape
./gradlew check

# Tag a version
git tag v0.0.0

# Publish a new build - BE MINDFUL OF SHELL HISTORY PRESERVING ENVIRONMENT VARIABLES
RELEASE=1 ./gradlew \
    build \
    publishToSonatype \
    closeAndReleaseSonatypeStagingRepository \
    --no-configuration-cache # Sonatype plugin fails configuration cache

# Push tags to github
git push --tags

# Create a new release: https://github.com/Nava2/kaff4/releases
# Update version to next patch version in `build-logic/src/main/kotlin/task-ranking.versioning.gradle.kts`,
#    e.g. `0.0.3-SNAPSHOT`
```
