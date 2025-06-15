package com.arena.game.entity.champion;

import com.arena.game.entity.EntityCollider;
import com.arena.game.entity.EntityNavMeshAgent;
import com.arena.game.entity.EntityRigidbody;
import com.arena.game.entity.EntityTransform;

/**
 * This class represents a champion in the Arena game.
 */
class Champion {
    EntityRigidbody rigidbody;
    EntityCollider collider;
    EntityTransform transform;
    EntityNavMeshAgent navMeshAgent;

    /**
     * Constructs a Champion with default properties.
     */
    Champion(float scale) {
        rigidbody = new EntityRigidbody();
        rigidbody.setKinematic(true);

        collider = new EntityCollider();
        collider.setEnabled(true);

        navMeshAgent = new EntityNavMeshAgent();
        navMeshAgent.setEnabled(true);

        transform = new EntityTransform();
        transform.setScale(scale);
    }

    public EntityRigidbody getRigidbody() {
        return rigidbody;
    }

    public EntityCollider getCollider() {
        return collider;
    }

    public EntityTransform getTransform() {
        return transform;
    }

    public EntityNavMeshAgent getNavMeshAgent() {
        return navMeshAgent;
    }
}
