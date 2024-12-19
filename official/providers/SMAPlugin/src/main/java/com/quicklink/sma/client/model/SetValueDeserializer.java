package com.quicklink.sma.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.quicklink.sma.client.model.DataResponse.SetValue;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SetValueDeserialier - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 08/10/2024
 */
public class SetValueDeserializer implements JsonDeserializer<SetValue> {

  @Override
  public SetValue deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();

    // Creare un'istanza di Persona
    String time = null;

    // Deserializzare i campi "nome" e "età"
    if (jsonObject.has("time")) {
      time = jsonObject.get("time").getAsString();
    }

    Map<String, Double> unknown = new LinkedHashMap<>();


    // Mettere tutti gli altri campi nella mappa
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      var key = entry.getKey();
      var value = context.deserialize(entry.getValue(), Object.class);

      if(value instanceof Double valueFloat) {
        unknown.put(key, valueFloat);
      } else if(value instanceof Map<?, ?> valueMap) {
        unknown.put(key, (Double) valueMap.get("value"));
      }
    }

    return new SetValue(time, unknown);
  }
}
