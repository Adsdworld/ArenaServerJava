package com.arena.game.zone;

import com.arena.game.entity.LivingEntity;
import com.arena.utils.Vector2f;

public class ZoneCone implements Zone {
    private final float distance;
    private final float angleDeg;

    public ZoneCone(float distance, float angleDeg) {
        this.distance = distance;
        this.angleDeg = angleDeg;
    }

    @Override
    public boolean isInZone(LivingEntity attacker, LivingEntity target) {
        Vector2f attackerPos = new Vector2f(attacker.getPosX(), attacker.getPosZ());
        Vector2f targetPos = new Vector2f(target.getPosX(), target.getPosZ());

        // Vecteur de l’attaquant vers la cible
        Vector2f toTarget = targetPos.sub(attackerPos);
        float distanceToTarget = toTarget.len();

        System.out.println("========== [ZoneCone Debug] ==========");
        System.out.println("Attacker Position      : (" + attackerPos.x + ", " + attackerPos.y + ")");
        System.out.println("Target Position        : (" + targetPos.x + ", " + targetPos.y + ")");
        System.out.println("Vector to Target       : (" + toTarget.x + ", " + toTarget.y + ")");
        System.out.println("Distance to Target     : " + distanceToTarget + " / max: " + distance);

        if (distanceToTarget > this.distance) {
            System.out.println("🟥 Target is out of range.");
            System.out.println("=======================================");
            return false;
        }

        float rotationY = attacker.getRotationY();
        Vector2f attackerDir = Vector2f.rotationToDirection(rotationY);

        System.out.println("Attacker Rotation Y    : " + rotationY);
        System.out.println("Attacker Dir Vector    : (" + attackerDir.x + ", " + attackerDir.y + ")");

        float dot = attackerDir.dot(toTarget);
        float len1 = attackerDir.len();
        float len2 = toTarget.len();
        float cos = dot / (len1 * len2);
        cos = Math.max(-1f, Math.min(1f, cos));
        float angleBetween = (float) Math.toDegrees(Math.acos(cos));

        System.out.println("Dot product            : " + dot);
        System.out.println("Len AttackerDir        : " + len1);
        System.out.println("Len ToTarget           : " + len2);
        System.out.println("Cosine(angle)          : " + cos);
        System.out.println("Angle Between Vectors  : " + angleBetween + "° / max: " + (angleDeg / 2f) + "°");

        boolean inCone = angleBetween <= (angleDeg / 2f);
        System.out.println("✅ Is in Cone           : " + inCone);
        System.out.println("=======================================");

        if (!inCone) {
            inCone = new ZoneCircle(distance/3f).isInZone(attacker, target);
        }

        return inCone;
    }


}
