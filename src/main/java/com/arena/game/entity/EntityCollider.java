package com.arena.game.entity;

public class EntityCollider {

    boolean enabled;

    public EntityCollider() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
