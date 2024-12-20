# Gradle Setup

Follow these steps to set up your environment for EasyML plugin development using Gradle.


## Requirements

Ensure you have the following prerequisites installed:

- **Java 17**

---

## Installation

### Step 1: Add the JitPack Repository
Include the JitPack repository in your Gradle build file (`build.gradle`):

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

---

### Step 2: Add the Dependency
Add the EasyML Plugins API dependency to your `build.gradle` file:

```groovy
compileOnly("com.github.qlsolutions.easyml-plugins:api:0.10.1")
```

---

### Step 3: Start Developing Your Plugin
Once the setup is complete, you can begin creating your [first plugin](../hello-world.md).

Enjoy extending EasyML with custom functionality!