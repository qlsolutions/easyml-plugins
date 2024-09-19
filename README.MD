EasyMl-Plugins is an API that allows adding functionalities within EasyML, in the backend (also
referred to as easyml-app).

At the moment, plugins of the following types can be added:

- Provider: will aim to return data within a certain period of time.
- Hook: send the details regarding a found anomaly to an external system

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
  <version>0.1.6</version>
  <scope>provided</scope>
</dependency>
```

#### Gradle

```kotlin
compileOnly("com.github.qlsolutions.easyml-plugins:api:0.1.6")
```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

After importing the API (excluding it from the generated JAR), create a class that extends
ProviderPlugin or HookPlugin. You can take a look at our official plugins located in the "examples"
folder.

After building (external libraries can also be included), upload the JAR file to the
/easyml/plugins/{hooks/providers} directory.
Then, start easyml-app and to ensure it's loaded, simply check the console. Otherwise, make a call
to the [endpoint]() in the Swagger documentation.
