package com.arena.game.handler;

import com.arena.player.ActionEnum;

import java.util.Map;

/**
 * This class contains a map of action handlers for the Arena game.
 */
public class HandlersGame {
    public static final Map<ActionEnum, IMessageHandler> HANDLERS = Map.of(
            ActionEnum.CreateGame, new CreateGameHandler(),
            ActionEnum.Join, new JoinHandler(),
            ActionEnum.CloseGame, new CloseGameHandler(),
            ActionEnum.PlayerStateUpdate, new PlayerStateUpdateHandler()
    );
}

