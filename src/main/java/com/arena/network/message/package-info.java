/**
 * This package contains classes related to the network messaging system
 * used in the Arena game framework.
 *
 * <p>The main class, {@link com.arena.network.message.Message}, represents
 * a network message that encapsulates an action performed by a player or
 * entity in the game. Each message includes:
 * <ul>
 *   <li>A unique identifier (UUID) to identify the sender or the entity involved.</li>
 *   <li>An {@link com.arena.player.ActionEnum} specifying the type of action.</li>
 *   <li>A {@link com.arena.game.GameNameEnum} indicating the game context.</li>
 *   <li>A timestamp representing when the action occurred, allowing message ordering.</li>
 *   <li>A reference to a {@link com.arena.game.entity.LivingEntity}, which represents
 *       the game entity involved in the action.</li>
 * </ul>
 *
 * <p>Messages are comparable based on their timestamps to enable
 * chronological sorting and processing in the networking logic.
 *
 * <p>The package supports serialization and deserialization of messages
 * using Gson for JSON conversion.
 *
 * @author A.SALLIER
 * @date 2025-06-15
 */
package com.arena.network.message;