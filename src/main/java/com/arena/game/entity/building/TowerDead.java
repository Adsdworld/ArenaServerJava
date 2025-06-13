package com.arena.game.entity.building;

import com.arena.game.entity.*;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.utils.Logger;
import com.arena.utils.Position;
import com.arena.utils.Vector3f;

public class TowerDead extends LivingEntity {
    private String skinAnimationIdle = "None";

    public TowerDead(String id, int team) {
        super(id, 0, team, "TowerDead");

        this.setAttackDamage(0);

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(true);
        this.setRigidbody(rigidbody);

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.setCollider(collider);

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(false);
        this.setNavMeshAgent(navMeshAgent);

        EntityTransform transform = new EntityTransform();
        transform.setScale(13f);
        this.setTransform(transform);

        this.setSkinScale(0.004f);
        this.setSkinPos(new Vector3f(0.0f, -0.65f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());
    }

    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationIdle;
    }
}
