package com.arena.game.entity;

/* Global entity interactions in Unity */
public class EntityRigidbody {

    /**
     * Indicates whether the rigidbody is kinematic.
     * A kinematic rigidbody is not affected by forces, gravity, or collisions.
     */
    boolean isKinematic;

    public EntityRigidbody() {
        this.isKinematic = true;
    }

    public void setKinematic(boolean isKinematic) {
        this.isKinematic = isKinematic;
    }

    public boolean isKinematic() {
        return isKinematic;
    }
}
