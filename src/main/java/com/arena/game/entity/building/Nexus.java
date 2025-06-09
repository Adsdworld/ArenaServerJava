package com.arena.game.entity.building;

import com.arena.game.entity.*;
import com.arena.utils.Vector3f;

public class Nexus extends LivingEntity {
    public Nexus(String id, int team) {
        super(id, 10000, team, "Nexus");

        this.attackDamage = 0;

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(true);
        this.rigidbody = rigidbody;

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.collider = collider;

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(false);
        this.navMeshAgent = navMeshAgent;

        EntityTransform transform = new EntityTransform();
        transform.setScale(10f);
        this.setTransform(transform);

        this.posY = 5f;

        this.setSkinScale(0.005f);
        this.setSkinPos(new Vector3f(0.0f, -1.03f, 0.0f));
    }
}
