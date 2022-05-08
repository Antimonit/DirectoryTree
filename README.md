[![Maven Central](https://img.shields.io/maven-central/v/io.github.antimonit/directory-tree.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.antimonit%22%20AND%20a:%22directory-tree%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CircleCI](https://circleci.com/gh/Antimonit/DirectoryTree/tree/main.svg?style=shield)](https://circleci.com/gh/Antimonit/DirectoryTree/tree/main)
[![codecov](https://codecov.io/gh/Antimonit/DirectoryTree/branch/main/graph/badge.svg?token=ZK4VJZSPFI)](https://codecov.io/gh/Antimonit/DirectoryTree)

# DirectoryTree

A tiny Kotlin DSL to pretty-print hierarchies of directories and files.

```kotlin
val hierarchy = directory {
    directory("featureOne") {
        file("build.gradle.kts")
    }
    directory("featureTwo") {
        file("build.gradle.kts")
    }
    directory("gradle-common") {
        file("build.gradle.kts")
        // optional shorthand form for multiple nested directories
        directory("src", "main", "kotlin") {
            directory("com", "sample", "gradle") {
                // optional shorthand form for a single file in one or more nested directories
                file("android", "library.gradle.kts")
                file("jacoco", "android.gradle.kts")
                file("sonarqube", "android.gradle.kts")
            }
        }
    }
}

println(hierarchy)
```

```
├── featureOne
│   ╰── build.gradle.kts
├── featureTwo
│   ╰── build.gradle.kts
╰── gradle-common
    ├── build.gradle.kts
    ╰── src
        ╰── main
            ╰── kotlin
                ╰── com
                    ╰── sample
                        ╰── gradle
                            ├── android
                            │   ╰── library.gradle.kts
                            ├── jacoco
                            │   ╰── android.gradle.kts
                            ╰── sonarqube
                                ╰── android.gradle.kts
```