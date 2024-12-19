# Hello World

*Your first plugin.*

## Start programming

**1.** Register HoloEasy api.

```java
@Override
public void onEnable() {
    HoloEasy.bind(plugin, PacketImpl.ProtocolLib /* PacketImpl.PacketEvents */ );
}
```

<br />


**2.** Define your first Plugin class.

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

<br />


**3.** Now you can do the build and the .jar file you can place it in `/easyml/plugins/providers`.

**4.** Congratulations! You have created your first plugin.