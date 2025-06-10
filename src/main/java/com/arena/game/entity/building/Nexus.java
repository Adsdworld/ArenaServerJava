package com.arena.game.entity.building;

import com.arena.game.entity.*;
import com.arena.utils.Vector3f;

public class Nexus extends LivingEntity {
    private String skinAnimationIdle = "Idle1_Base";


    public Nexus(String id, int team) {
        super(id, 10000, team, "Nexus");

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
        transform.setScale(26f);
        this.setTransform(transform);


        this.setSkinScale(0.0015f);
        this.setSkinPos(new Vector3f(0.0f, 0.578f, 0.0f));
        this.setSkinAnimation(getSkinAnimationForIdle());
    }
    @Override
    public String getSkinAnimationForIdle() {
        return skinAnimationIdle;
    }
}
