package com.arena.game.handler;

import com.arena.player.ActionEnum;

import java.util.Map;

/**
 * This class contains a map of action handlers for the Arena game.
 */
public class HandlersCast {
    public static final Map<ActionEnum, IMessageHandler> HANDLERS = Map.of(
            ActionEnum.CastQ, new CastQHandler(),
            ActionEnum.CastW, new CastWHandler(),
            ActionEnum.CastE, new CastEHandler(),
            ActionEnum.CastR, new CastRHandler()
    );
}

