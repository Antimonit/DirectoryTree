![Maven Central](https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.antimonit/directory-tree.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.antimonit%22%20AND%20a:%22directory-tree%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CircleCI](https://img.shields.io/circleci/build/gh/Antimonit/DirectoryTree?label=CircleCI)](https://circleci.com/gh/Antimonit/DirectoryTree/tree/main)
[![Codecov](https://img.shields.io/codecov/c/gh/Antimonit/DirectoryTree?label=Codecov)](https://codecov.io/gh/Antimonit/DirectoryTree)

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

## Configuration

Additional configuration can be specified by passing a `DirectoryContext` to the top-level `directory` function.

```kotlin
directory(
    context = DirectoryContext(
        separators = Separators(
            empty = "    ",
            pipe = "│   ",
            split = "├── ",
            corner = "╰── ",
        ),
        sortMode = SortMode.NONE,
        directoriesFirst = false,
        compactMode = CompactMode.NONE,
    )
) {
    file("file.txt")
}
```

### Separators
Customize separators.

<table>
<tr>
<td>

```
empty  = "    "
pipe   = "│   "
split  = "├── "
corner = "╰── "
```

</td>
<td>

```
empty  = "   "
pipe   = "║  "
split  = "╟─╴"
corner = "╙─╴"
```

</td>
<td>

```
empty  = "     "
pipe   = "│    "
split  = "┝━━━ "
corner = "┕━━━ "
```

</td>

</tr>
<tr>
<td>

```
├── one         
│   ╰── a.txt 
╰── two         
    ╰── b.txt 
```

</td>
<td>

```
╟─╴one         
║  ╙─╴a.txt 
╙─╴two         
   ╙─╴b.txt 
```

</td>
<td>

```
┝━━━ one
│    ┕━━━ a.txt
┕━━━ two
     ┕━━━ b.txt
```

</td>
</tr>
</table>


### Sort mode
* `NONE` - Do not sort.
* `ASC` - Sort ascending by file/directory names.
* `DESC` - Sort descending by file/directory names.

<table>
<tr>
<td>

`NONE`

</td>
<td>

`ASC`

</td>
<td>

`DESC`

</td>
</tr>

<tr>
<td>

    ├── one
    ├── two
    ╰── three

</td>
<td>

    ├── one
    ├── three
    ╰── two

</td>
<td>

    ├── two
    ├── three
    ╰── one

</td>
</tr>
</table>

### Directories first
* `false` - Do not reorganize files/directories.
* `true` - Place directories before files.

<table>
<tr>
<td>

`false`

</td>
<td>

`true`

</td>
</tr>
<tr>
<td>

    ├── z.txt
    ├── one
    │   ╰── file.txt
    ╰── a.txt

</td>
<td>

    ├── one
    │   ╰── file.txt
    ├── z.txt
    ╰── a.txt

</td>
</tr>
</table>

### Compact mode
* `NONE` - No directories and files will be merged and each will display on a separate line.
* `EXACT` - Directories and files defined in a single call to `directory` or `file` will be merged.
* `DIRECTORIES` - Directories with no sibling directories or files will be merged with their parents.
* `ALL` - Directories and files with no sibling directories or files will be merged with their parents.

```kotlin
directory(
    DirectoryContext(compactMode = compactMode)
) {
    directory("one") {
        file("two", "file.txt")
    }
}

```

<table>
<tr>
<td>

`NONE`

</td>
<td>

`EXACT`

</td>
<td>

`DIRECTORIES`

</td>
<td>

`ALL`

</td>
</tr>
<tr>
<td>

    ╰── one
        ╰── two
            ╰── file.txt

</td>
<td>

    ╰── one
        ╰── two/file.txt

</td>
<td>

    ╰── one/two
        ╰── file.txt

</td>
<td>

    ╰── one/two/file.txt

</td>
</tr>
</table>

# Download

The library is available from the MavenCentral repository as a Kotlin Multiplatform library targeting JVM, JS and 
native:

```kotlin
implementation("io.github.antimonit:directory-tree:1.3.0")
```
