package com.arena.utils.json;

import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.game.entity.building.Tower;
import com.arena.game.entity.building.TowerDead;
import com.arena.game.entity.champion.Garen;
import com.arena.utils.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return validateJson(json, clazz);
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax: " + e.getMessage());
        } catch (JsonIOException e) {
            System.err.println("I/O error during JSON deserialization: " + e.getMessage());
        }
        return null;
    }

    @Override
    public JsonElement toJsonTree(Object parsedObject) {
        try {
            return gson.toJsonTree(parsedObject);
        } catch (JsonIOException e) {
            System.err.println("I/O error during JSON tree conversion: " + e.getMessage());
        }
        return null;
    }

    /**
     * Validates the deserialization of a JSON string, by verifying that all fields in the original JSON are present in the parsed object.
     *
     * @param messageJson The JSON string to validate.
     * @param clazz The class type to which the JSON string should be deserialized.
     * @param <T> The generic type representing the target class of deserialization.
     * @return <T> The parsed object of type T.
     * @implNote This method deserializes the JSON string into an object of type T, then compares the original JSON keys with the keys of the parsed object.
     * @author A.SALLIER
     * @date 2025-06-15
     */
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
