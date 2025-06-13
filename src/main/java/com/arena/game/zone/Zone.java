package com.arena.game.zone;

import com.arena.game.entity.LivingEntity;

public interface Zone {
    boolean isInZone(LivingEntity attacker, LivingEntity target);
}
