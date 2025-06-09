package com.arena.game.entity;

import com.arena.utils.Position;

import java.util.Map;

public class EntityPositions {

    // Spawn joueurs
    public static final Position BLUE_SPAWN = new Position(272.9823f, 5.0f, 300.6143f, 0f);
    public static final Position RED_SPAWN = new Position(723.0251f, 5.0f, 752.9824f, 0f);
    public static final Position CENTER_SPAWN = new Position(493.0f, 5.0f, 530.0f, 0f);

    /*
     * Nearest from the nexus from top to bottom
     */
    public static final Map<String, Position> BLUE_TOWERS = Map.ofEntries(
            Map.entry("T4_TOP_BLUE", new Position(312.82f, 5f, 357.67f, 180f)),
            Map.entry("T3_TOP_BLUE", new Position(294.68f, 5f, 423.74f, 135f)),
            Map.entry("T2_TOP_BLUE", new Position(304.52f, 5f, 499.99f, 135f)),
            Map.entry("T1_TOP_BLUE", new Position(286.94f, 5f, 621.03f, 135f)),
            Map.entry("T4_BOT_BLUE", new Position(327.72f, 5f, 342.34f, 180f)),
            Map.entry("T3_MID_BLUE", new Position(375.67f, 5f, 403.40f, 180f)),
            Map.entry("T2_MID_BLUE", new Position(419.68f, 5f, 438.94f, 180f)),
            Map.entry("T1_MID_BLUE", new Position(445.92f, 5f, 490.81f, 180f)),
            Map.entry("T3_BOT_BLUE", new Position(396.33f, 5f, 324.44f, 225f)),
            Map.entry("T2_BOT_BLUE", new Position(478.89f, 5f, 332.68f, 225f)),
            Map.entry("T1_BOT_BLUE", new Position(596.47f, 5f, 316.67f, 225f))
    );

    public static final Map<String, Position> RED_TOWERS = Map.ofEntries(
            Map.entry("T4_TOP_RED", new Position(665.27f, 5f, 709.47f, 0f)),
            Map.entry("T3_TOP_RED", new Position(597.55f, 5f, 726.01f, 45f)),
            Map.entry("T2_TOP_RED", new Position(512.38f, 5f, 718.81f, 45f)),
            Map.entry("T1_TOP_RED", new Position(396.16f, 5f, 733.47f, 45f)),
            Map.entry("T4_BOT_RED", new Position(679.5f, 5f, 694.41f, 0f)),
            Map.entry("T3_MID_RED", new Position(617.9f, 5f, 646.95f, 0f)),
            Map.entry("T2_MID_RED", new Position(572.77f, 5f, 610.8f, 0f)),
            Map.entry("T1_MID_RED", new Position(546.98f, 5f, 559.76f, 0f)),
            Map.entry("T3_BOT_RED", new Position(698.42f, 5f, 628.12f, 315f)),
            Map.entry("T2_BOT_RED", new Position(687.22f, 5f, 550.48f, 315f)),
            Map.entry("T1_BOT_RED", new Position(704.34f, 5f, 428.93f, 315f))
    );

    public static final Map<String, Position> BLUE_INHIBITORS = Map.of(
            "INHIB_TOP_BLUE", new Position(295.19f, 5f, 400.44f, 0f),
            "INHIB_MID_BLUE", new Position(360.45f, 5f, 389.32f, 0f),
            "INHIB_BOT_BLUE", new Position(371.75f, 5f, 324.39f, 0f)
    );

    public static final Map<String, Position> RED_INHIBITORS = Map.of(
            "INHIB_TOP_RED", new Position(623.69f, 5f, 726.13f, 0f),
            "INHIB_MID_RED", new Position(632.51f, 5f, 662.37f, 0f),
            "INHIB_BOT_RED", new Position(698.56f, 5f, 650.66f, 0f)
    );

    public static final Map<String, Position> BLUE_NEXUS = Map.of(
            "NEXUS_BLUE", new Position(307.62f, 5f, 337.96f, 0f)
    );

    public static final Map<String, Position> RED_NEXUS = Map.of(
            "NEXUS_RED", new Position(685.20f, 5f, 713.00f, 0f)
    );
}
