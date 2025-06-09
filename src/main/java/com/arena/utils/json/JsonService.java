package com.arena.utils.json;

import com.google.gson.JsonElement;

public class JsonService {

    private static IJson worker;

    public String toJson(Object object) {
        return worker.toJson(object);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return worker.fromJson(json, clazz);
    }

    public static void setWorker(IJson worker) {
        JsonService.worker = worker;
    }

    public static IJson getWorker() {
        return worker;
    }

    public JsonElement toJsonTree(Object parsedObject) {
        return worker.toJsonTree(parsedObject);
    }
}
