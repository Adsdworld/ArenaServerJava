package com.arena.game.zone;

import com.arena.game.entity.LivingEntity;

/**
 * This interface defines a zone in the Arena game.
 * A zone is a specific area where certain conditions apply, such as whether an attacker can target a living entity.
 */
public interface Zone {
    /**
     * Checks if the attacker is within the zone of the target.
     *
     * @param attacker The living entity that is attacking.
     * @param target The living entity that is being targeted.
     * @return true if the attacker is within the zone of the target, false otherwise.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    boolean isInZone(LivingEntity attacker, LivingEntity target);
}
