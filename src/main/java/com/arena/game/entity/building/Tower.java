package com.arena.game.entity.building;

import com.arena.game.entity.Entity;
import com.arena.game.entity.LivingEntity;

public class Tower extends LivingEntity {
    public Tower(String id) {
        super(id, 2000, 0, "Tower");
        this.attackDamage = 100;
    }

    public void shoot(Entity target) {
        // logique d’attaque de la tour
    }
}
