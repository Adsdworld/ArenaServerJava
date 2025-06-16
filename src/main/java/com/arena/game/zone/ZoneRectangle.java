package com.arena.game.zone;

import com.arena.game.entity.LivingEntity;
import com.arena.utils.Vector2f;

public class ZoneRectangle implements Zone {
    private final float width;
    private final float length;

    public ZoneRectangle(float width, float length) {
        this.width = width;
        this.length = length;
    }

    @Override
    public boolean isInZone(LivingEntity attacker, LivingEntity target) {
        Vector2f attackerPos = new Vector2f(attacker.getPosX(), attacker.getPosZ());
        Vector2f targetPos = new Vector2f(target.getPosX(), target.getPosZ());

        Vector2f toTarget = targetPos.sub(attackerPos);

        Vector2f forward = Vector2f.rotationToDirection(attacker.getRotationY());
        Vector2f right = new Vector2f(forward.y, -forward.x);

        float forwardDist = toTarget.dot(forward);
        float rightDist = toTarget.dot(right);

        return (forwardDist >= 0 && forwardDist <= length && Math.abs(rightDist) <= width / 2f);
    }
}
