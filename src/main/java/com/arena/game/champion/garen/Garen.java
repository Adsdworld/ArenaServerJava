package com.arena.game.champion.garen;

import com.arena.game.champion.Champion;

public class Garen extends Champion {
    private final String name = "Garen";

    private Integer health = 620;
    private Integer maxHealth = 620;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getHealth() {
        return health;
    }

    @Override
    public Integer getMaxHealth() {
        return maxHealth;
    }
}
