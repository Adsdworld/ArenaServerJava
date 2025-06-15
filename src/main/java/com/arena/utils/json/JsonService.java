package com.arena.utils.json;

import com.google.gson.JsonElement;

public class JsonService {

    private static IJson worker;

    /**
     * Converts an object to its JSON representation.
     *
     * @param object the object to convert to JSON
     * @return a JSON string representation of the object
     * @implNote This method uses the worker instance of IJson to perform the conversion.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public String toJson(Object object) {
        return worker.toJson(object);
    }

    /**
     * Converts a JSON string to an object of the specified class.
     *
     * @param json the JSON string to convert
     * @param clazz the class type to which the JSON string should be deserialized
     * @param <T> the generic type representing the target class of deserialization
     * @return an object of type T parsed from the JSON string
     * @implNote This method uses the worker instance of IJson to perform the conversion.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        return worker.fromJson(json, clazz);
    }

    public static void setWorker(IJson worker) {
        JsonService.worker = worker;
    }

    public static IJson getWorker() {
        return worker;
    }

    /**
     * Converts an object to a JSON tree representation.
     *
     * @param parsedObject the object to convert to a JSON tree
     * @return a JsonElement representing the object as a JSON tree
     * @implNote This method uses the worker instance of IJson to perform the conversion.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public JsonElement toJsonTree(Object parsedObject) {
        return worker.toJsonTree(parsedObject);
    }
}
