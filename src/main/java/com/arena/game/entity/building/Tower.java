package com.arena.game.entity.building;

import com.arena.game.entity.*;
import com.arena.utils.Vector3f;

public class Tower extends LivingEntity {
    public Tower(String id, int team) {
        super(id, 2000, team, "Tower");

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

        EntityTransform transform = new EntityTransform();
        transform.setScale(13f);
        this.setTransform(transform);

        this.setSkinScale(0.004f);
        this.setSkinPos(new Vector3f(0.0f, -1.03f, 0.0f));
    }
}
