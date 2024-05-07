EasyMl-Plugins is an API that allows adding functionalities within EasyML, in the backend (also referred to as easyml-app).

At the moment, plugins of the following types can be added:

- Provider: will aim to return data within a certain period of time. 
- Hook: ...

## Quickstart

### Requirements
* Java 17
* Maven/Gradle

### Add dependency
#### Maven
```xml
<dependency>
  <groupId>com.github.qlsolutions.easyml-plugins</groupId>
  <artifactId>api</artifactId>
  <version>${VERSION}</version>
  <scope>provided</scope>
</dependency>
```

#### Gradle
```kotlin
implementation("com.github.qlsolutions.easyml-plugins:api:${VERSION}")
```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

...