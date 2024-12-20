# Hello World

*Your first plugin.*

## Step 1: Define Your First Plugin Class

Begin by defining your first plugin class. Here’s an example implementation in Java:

```java
public class HelloWorldPlugin extends ProviderPlugin {

  public HelloWorldPlugin() {
    super("Hello World", "1.0.0");
  }

  @Override
  public void onEnable() {
    getLogger().info("Plugin Hello World enabled");
  }

  @Override
  public void onCreate(@NotNull UUID id) {

  }

  @Override
  public @NotNull List<Serie> getSeries(@NotNull UUID id) {
    return List.of(
        new Serie("temp", "temperature")
    );
  }

  @Override
  public @NotNull LinkedList<TimedValue> getSerieData(@NotNull UUID id, @NotNull String serieId,
      @NotNull Instant start, @NotNull Instant end) {
    var now = Instant.now();

    var list = new LinkedList<TimedValue>();
    list.add(new TimedValue(now, 25.0));
    list.add(new TimedValue(now.plusSeconds(60), 26.0));
    list.add(new TimedValue(now.plusSeconds(120), 27.0));
    list.add(new TimedValue(now.plusSeconds(180), 28.0));
    return list;
  }

  @Override
  public boolean status(@NotNull UUID id) {
    return true;
  }
}
```

### Key Methods in the Plugin Class:
1. **`onEnable()`**: Logs a message when the plugin is enabled.
2. **`onCreate(UUID id)`**: Reserved for initialization tasks when creating a DataProvider using this plugin.
3. **`getSeries(UUID id)`**: Returns a list of series available for the plugin (e.g., temperature).
4. **`getSerieData(UUID id, String serieId, Instant start, Instant end)`**: Provides data for a specific series within the given time range.
5. **`status(UUID id)`**: Returns the status of the plugin (always `true` in this example).

---

## Step 2: Build the Plugin

After defining your plugin class, build your project to generate the `.jar` file.

Place the resulting `.jar` file in the following directory:  
`/easyml/plugins/providers`

---

## Step 3: Congratulations!

You’ve successfully created and deployed your first plugin. 🎉

Your plugin is now ready to be used on EasyML!