/**
 * Provides utility classes and interfaces for JSON serialization and deserialization,
 * mainly using the Gson library.
 * <p>
 * This package includes:
 * <ul>
 *     <li>{@link com.arena.utils.json.GsonWorker}: A concrete implementation of {@link com.arena.utils.json.IJson},
 *     using a configured {@link com.google.gson.GsonBuilder} to enable polymorphic serialization of game entities.</li>
 *     <li>{@link com.arena.utils.json.IJson}: An interface defining the basic methods for JSON transformation.</li>
 *     <li>{@link com.arena.utils.json.JsonService}: A static service that centralizes access to JSON serialization methods across the application.</li>
 * </ul>
 * <p>
 * This system enables safe and type-aware serialization of game entities such as
 * {@link com.arena.game.entity.champion.Garen}, {@link com.arena.game.entity.building.Tower}, etc.,
 * by supporting dynamic typing through a RuntimeTypeAdapterFactory.
 * <br>
 * It also includes a custom validation method via {@link com.arena.utils.json.GsonWorker#validateJson(String, Class)}
 * to detect unexpected fields or unknown enum values during deserialization.
 *
 * @author A.SALLIER
 * @version 1.0
 * @since 2025-06-15
 */
package com.arena.utils.json;