package com.quicklink.pluginservice.hooks;



import com.quicklink.pluginservice.KeyParam;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class HookContext {

  private final int idApp;
  private final String nameApp;
  private final Map<String, ?> parameters;


  public HookContext(int idApp, String nameApp, Map<String, ?> parameters) {
    this.idApp = idApp;
    this.nameApp = nameApp;
    this.parameters = parameters;
  }

  public int idApp() {
    return idApp;
  }

  public String nameApp() {
    return nameApp;
  }

  public <T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.get(key.getId());
  }

}
