package com.arena.game.entity;

public class EntityRigidbody {

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
