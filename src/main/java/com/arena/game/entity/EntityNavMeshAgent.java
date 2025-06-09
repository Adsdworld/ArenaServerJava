package com.arena.game.entity;

public class EntityNavMeshAgent {
    boolean enabled;

    public EntityNavMeshAgent() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isEnabled() {
        return enabled;
    }
}
