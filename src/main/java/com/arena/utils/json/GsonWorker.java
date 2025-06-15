package com.arena.utils.json;

import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.game.entity.building.Tower;
import com.arena.game.entity.building.TowerDead;
import com.arena.game.entity.champion.Garen;
import com.arena.utils.logger.Logger;
import com.google.gson.*;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.util.Map;

public class GsonWorker implements IJson {

    //private final Gson gson = new Gson();

    public final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory.of(LivingEntity.class, "RuntimeTypeAdapterFactoryClazz")
                            .registerSubtype(Garen.class, "Garen")
                            .registerSubtype(Tower.class, "Tower")
                            .registerSubtype(TowerDead.class, "TowerDead")
                            .registerSubtype(Inhibitor.class, "Inhibitor")
                            .registerSubtype(Nexus.class, "Nexus")

            )
            .create();


    public GsonWorker() {
        JsonService.setWorker(this);
    }

    @Override
    public String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (JsonIOException e) {
            System.err.println("I/O error during JSON serialization: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return validateJson(json, clazz);
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonIOException e) {
            System.err.println("I/O error during JSON deserialization: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JsonElement toJsonTree(Object parsedObject) {
        try {
            return gson.toJsonTree(parsedObject);
        } catch (JsonIOException e) {
            System.err.println("I/O error during JSON tree conversion: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public <T> T validateJson(String messageJson, Class<T> clazz) {
        JsonObject jsonRaw = JsonParser.parseString(messageJson).getAsJsonObject();

        T parsedObject = gson.fromJson(messageJson, clazz);

        JsonObject jsonParsed = gson.toJsonTree(parsedObject).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonRaw.entrySet()) {
            String key = entry.getKey();
            JsonElement rawValue = entry.getValue();

            if (rawValue == null || rawValue.isJsonNull()) {
                continue;
            }

            if (!jsonParsed.has(key) || jsonParsed.get(key).isJsonNull()) {
                Logger.warn("Data ignored or unrecognized enum: '" + key + "' = " + rawValue);
            }
        }

        return parsedObject;
    }
}
