package com.arena.game.entity.building;

import com.arena.game.entity.*;

public class Tower extends LivingEntity {
    public Tower(String id) {
        super(id, 2000, 0, "Tower");

        this.attackDamage = 100;

        EntityRigidbody rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(true);
        this.rigidbody = rigidbody;

        EntityCollider collider = new EntityCollider();
        collider.setEnabled(true);
        this.collider = collider;

        EntityNavMeshAgent navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(false);
        this.navMeshAgent = navMeshAgent;
    }
}
