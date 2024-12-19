# Gradle

## Requirements

The prerequisites:
- Java 17

## Installation

**1.** Add the JitPack repository to your build file.

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

<br />

**2.** Add the dependency.

```groovy
compileOnly("com.github.qlsolutions.easyml-plugins:api:0.10.1")
```

<br />

**3.** Once you’re done, you may now proceed creating your first plugin.