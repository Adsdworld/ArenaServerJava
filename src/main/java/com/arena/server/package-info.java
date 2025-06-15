/**
 * The {@code com.arena.server} package is responsible for managing server-side logic
 * of the multiplayer arena game. It handles player registration, game creation and destruction,
 * and the management of game states and entities within those games.
 * <p>
 * Core responsibilities include:
 * <ul>
 *     <li>Maintaining a singleton {@link com.arena.server.Server} instance</li>
 *     <li>Creating and closing game sessions identified by {@link com.arena.game.GameNameEnum}</li>
 *     <li>Registering and retrieving {@link com.arena.player.Player} instances</li>
 *     <li>Subscribing players to games and updating their states</li>
 *     <li>Associating entities such as {@link com.arena.game.entity.building.Tower},
 *         {@link com.arena.game.entity.building.Inhibitor}, and {@link com.arena.game.entity.building.Nexus}
 *         with game instances and positions using {@link com.arena.game.utils.EntityInit}</li>
 *     <li>Logging important server events using the {@link com.arena.utils.logger.Logger} utility</li>
 * </ul>
 * <p>
 * This package is the backbone of the multiplayer game environment, ensuring data integrity,
 * consistent state management, and synchronized player-game interaction.
 *
 * @author A.
 * @since 2025-06-07
 */
package com.arena.server;