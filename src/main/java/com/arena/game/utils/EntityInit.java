package com.arena.game.utils;

public class EntityInit {
    private final Position position;
    private final boolean isAttackable;
    private final String nextObjectiveId;

    public EntityInit(Position position, boolean isAttackable, String nextObjectiveId) {
        this.position = position;
        this.isAttackable = isAttackable;
        this.nextObjectiveId = nextObjectiveId;
    }

    public EntityInit(Position position, String nextObjectiveId) {
        this.position = position;
        this.isAttackable = false;
        this.nextObjectiveId = nextObjectiveId;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAttackable() {
        return isAttackable;
    }

    public String getNextObjectiveId() {
        return nextObjectiveId;
    }
}
