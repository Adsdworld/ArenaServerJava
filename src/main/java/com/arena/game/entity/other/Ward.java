package com.arena.game.entity.other;

import com.arena.game.entity.LivingEntity;

public class Ward extends LivingEntity {
    public Ward(String id) {
        super(id, 50, 0, "Ward");
        this.armor = 0;
        this.moveSpeed = 0;
    }
}
