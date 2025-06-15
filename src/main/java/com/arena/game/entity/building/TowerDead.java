package com.arena.game.entity.building;

import com.arena.game.entity.LivingEntity;
import com.arena.utils.Vector3f;

public class TowerDead extends LivingEntity {

    public TowerDead(String id, int team) {
        super(id, 0, team, "TowerDead");

        Building building = new Building(13f);

        this.setRigidbody(building.getRigidbody());

        this.setCollider(building.getCollider());

        this.setNavMeshAgent(building.getNavMeshAgent());

        this.setTransform(building.getTransform());

        this.setSkinScale(0.004f);
        this.setSkinPos(new Vector3f(0.0f, -0.65f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());
    }
}
