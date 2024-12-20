# Maven Setup

Follow these steps to set up your environment for EasyML plugin development using Maven.

## Requirements

Ensure you have the following prerequisites installed:

- **Java 17**

---

## Installation

### Step 1: Add the JitPack Repository
Include the JitPack repository in your Maven build file (`pom.xml`):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

---

### Step 2: Add the Dependency
Add the EasyML Plugins API dependency to your `pom.xml` file:

```xml
<dependency>
  <groupId>com.github.qlsolutions.easyml-plugins</groupId>
  <artifactId>api</artifactId>
  <version>0.10.1</version>
  <scope>provided</scope>
</dependency>
```

---

### Step 3: Start Developing Your Plugin
Once the setup is complete, you can begin creating your [first plugin](../hello-world.md).

Enjoy extending EasyML with custom functionality!