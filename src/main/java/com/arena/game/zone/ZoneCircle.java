package com.arena.game.zone;

import com.arena.game.entity.LivingEntity;
import com.arena.utils.Vector2f;

public class ZoneCircle implements Zone {
    float radius;

    public ZoneCircle(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean isInZone(LivingEntity attacker, LivingEntity target) {
        Vector2f attackerPos = new Vector2f(attacker.getPosX(), attacker.getPosZ());
        Vector2f targetPos = new Vector2f(target.getPosX(), target.getPosZ());

        return attackerPos.distTo(targetPos) <= radius;
    }
}
