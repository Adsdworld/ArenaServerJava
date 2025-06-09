package com.arena.utils.json;

import com.google.gson.JsonElement;

public interface IJson {

    /**
     * Converts an object to its JSON representation.
     *
     * @param object the object to convert
     * @return the JSON string representation of the object
     */
    String toJson(Object object);

    /**
     * Parses a JSON string into an object of the specified class.
     *
     * @param json  the JSON string to parse
     * @param clazz the class of the object to return
     * @param <T>   the type of the object
     * @return an instance of the specified class populated with data from the JSON string
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * Converts an object to a JSON tree structure.
     *
     * @param parsedObject the object to convert
     * @return a JSON element representing the object
     */
    JsonElement toJsonTree(Object parsedObject);
}
