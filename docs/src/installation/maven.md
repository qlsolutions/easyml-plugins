# Maven

## Requirements

The prerequisites:
- Java 17

## Installation

**1.** Add the JitPack repository to your build file.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

<br />

**2.** Add the dependency.

```xml
<dependency>
  <groupId>com.github.qlsolutions.easyml-plugins</groupId>
  <artifactId>api</artifactId>
  <version>0.10.1</version>
  <scope>provided</scope>
</dependency>
```

<br />

**3.** Once you’re done, you may now proceed creating your first plugin.