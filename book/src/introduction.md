# Introduction to EasyML Plugins

*Get familiar with EasyML Plugins.*


## What Is EasyML Plugins?

EasyML Plugins is an API that allows you to extend the functionality of `EasyML`.

Currently, plugins of the following types can be added:


### Native Plugins
Native plugins can be written in any language from the JVM ecosystem, such as **Java** or **Kotlin**. These plugins are placed in the `/easyml/plugins` directory and are loaded when EasyML starts.

For more details, refer to the [Native Plugins Guide](native.md).


### IoT Plugins
IoT plugins can be written in any language and interact with EasyML via **HTTP calls**.

For more details, refer to the [IoT Plugins Guide](iot.md).

---

## Key Difference Between Plugin Types

1. **Native Plugins**: EasyML calls the implementation provided by the plugin.
2. **IoT Plugins**: Plugin authors interact with EasyML by making HTTP calls.

---

Choose the plugin type that best suits your use case and start extending EasyML’s capabilities!