package com.arena.game.entity;

import com.arena.game.utils.EntityInit;
import com.arena.game.utils.Position;

import java.util.List;
import java.util.Map;

public class EntityPositions {

    public static final Position BLUE_SPAWN = new Position(272.9823f, 15.0f, 300.6143f, 0f);
    public static final Position RED_SPAWN = new Position(723.0251f, 15.0f, 752.9824f, 0f);
    public static final Position CENTER_SPAWN = new Position(493.0f, 15.0f, 530.0f, 0f);

    /*
     * Nearest from the nexus from top to bottom
     */
    public static final Map<String, EntityInit> BLUE_TOWERS = Map.ofEntries(
            Map.entry("T4_TOP_BLUE", new EntityInit(new Position(312.82f, 9.37f, 357.67f, 180f), "NEXUS_BLUE")),
            Map.entry("T3_TOP_BLUE", new EntityInit(new Position(294.68f, 9.37f, 423.74f, 135f), "INHIB_TOP_BLUE")),
            Map.entry("T2_TOP_BLUE", new EntityInit(new Position(304.52f, 8.07f, 499.99f, 135f), "T3_TOP_BLUE")),
            Map.entry("T1_TOP_BLUE", new EntityInit(new Position(286.94f, 8.07f, 621.03f, 135f), true, "T2_TOP_BLUE")),
            Map.entry("T4_BOT_BLUE", new EntityInit(new Position(327.72f, 9.37f, 342.34f, 180f), "NEXUS_BLUE")),
            Map.entry("T3_MID_BLUE", new EntityInit(new Position(375.67f, 9.37f, 403.40f, 180f), "INHIB_MID_BLUE")),
            Map.entry("T2_MID_BLUE", new EntityInit(new Position(419.68f, 8.07f, 438.94f, 180f), "T3_MID_BLUE")),
            Map.entry("T1_MID_BLUE", new EntityInit(new Position(445.92f, 8.07f, 490.81f, 180f), true, "T2_MID_BLUE")),
            Map.entry("T3_BOT_BLUE", new EntityInit(new Position(396.33f, 9.37f, 324.44f, 225f), "INHIB_BOT_BLUE")),
            Map.entry("T2_BOT_BLUE", new EntityInit(new Position(478.89f, 8.07f, 332.68f, 225f), "T3_BOT_BLUE")),
            Map.entry("T1_BOT_BLUE", new EntityInit(new Position(596.47f, 8.07f, 316.67f, 225f), true, "T2_BOT_BLUE"))
    );

    public static final Map<String, EntityInit> RED_TOWERS = Map.ofEntries(
            Map.entry("T4_TOP_RED", new EntityInit(new Position(665.27f, 9.37f, 709.47f, 0f), "NEXUS_RED")),
            Map.entry("T3_TOP_RED", new EntityInit(new Position(597.55f, 9.37f, 726.01f, 45f), "INHIB_TOP_RED")),
            Map.entry("T2_TOP_RED", new EntityInit(new Position(512.38f, 8.07f, 718.81f, 45f), "T3_TOP_RED")),
            Map.entry("T1_TOP_RED", new EntityInit(new Position(396.16f, 8.07f, 733.47f, 45f), true, "T2_TOP_RED")),
            Map.entry("T4_BOT_RED", new EntityInit(new Position(679.5f, 9.37f, 694.41f, 0f), "NEXUS_RED")),
            Map.entry("T3_MID_RED", new EntityInit(new Position(617.9f, 9.37f, 646.95f, 0f), "INHIB_MID_RED")),
            Map.entry("T2_MID_RED", new EntityInit(new Position(572.77f, 8.07f, 610.8f, 0f), "T3_MID_RED")),
            Map.entry("T1_MID_RED", new EntityInit(new Position(546.98f, 8.07f, 559.76f, 0f), true, "T2_MID_RED")),
            Map.entry("T3_BOT_RED", new EntityInit(new Position(698.42f, 9.37f, 628.12f, 315f), "INHIB_BOT_RED")),
            Map.entry("T2_BOT_RED", new EntityInit(new Position(687.22f, 8.07f, 550.48f, 315f), "T3_BOT_RED")),
            Map.entry("T1_BOT_RED", new EntityInit(new Position(704.34f, 8.07f, 428.93f, 315f), true, "T2_BOT_RED"))
    );

    public static final Map<String, EntityInit> BLUE_INHIBITORS = Map.of(
            "INHIB_TOP_BLUE", new EntityInit(new Position(295.19f, -7.36f, 400.44f, 0f), List.of("T4_BOT_BLUE", "T4_TOP_BLUE")),
            "INHIB_MID_BLUE", new EntityInit(new Position(360.45f, -7.36f, 389.32f, 0f), List.of("T4_BOT_BLUE", "T4_TOP_BLUE")),
            "INHIB_BOT_BLUE", new EntityInit(new Position(371.75f, -7.36f, 324.39f, 0f), List.of("T4_BOT_BLUE", "T4_TOP_BLUE"))
    );

    public static final Map<String, EntityInit> RED_INHIBITORS = Map.of(
            "INHIB_TOP_RED", new EntityInit(new Position(623.69f, -7.36f, 726.13f, 0f), List.of("T4_BOT_RED", "T4_TOP_RED")),
            "INHIB_MID_RED", new EntityInit(new Position(632.51f, -7.36f, 662.37f, 0f), List.of("T4_BOT_RED", "T4_TOP_RED")),
            "INHIB_BOT_RED", new EntityInit(new Position(698.56f, -7.36f, 650.66f, 0f), List.of("T4_BOT_RED", "T4_TOP_RED"))
    );

    public static final Map<String, EntityInit> BLUE_NEXUS = Map.of(
            "NEXUS_BLUE", new EntityInit(new Position(307.62f, -13.5f, 337.96f, 0f), "None")
    );

    public static final Map<String, EntityInit> RED_NEXUS = Map.of(
            "NEXUS_RED", new EntityInit(new Position(685.20f, -13.5f, 713.00f, 0f), "None")
    );
}
