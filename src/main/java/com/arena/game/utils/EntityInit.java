package com.arena.game.utils;

import java.util.Collection;
import java.util.List;

public class EntityInit {
    private final Position position;
    private final boolean isAttackable;
    private final Collection<String> nextObjectiveId;

    public EntityInit(Position position, boolean isAttackable, String nextObjectiveId) {
        this.position = position;
        this.isAttackable = isAttackable;
        this.nextObjectiveId = List.of(nextObjectiveId);
    }

    public EntityInit(Position position, Collection<String> nextObjectiveId) {
        this.position = position;
        this.isAttackable = false;
        this.nextObjectiveId = nextObjectiveId;
    }

    public EntityInit(Position position, String nextObjectiveId) {
        this.position = position;
        this.isAttackable = false;
        this.nextObjectiveId = List.of(nextObjectiveId);
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAttackable() {
        return isAttackable;
    }

    public Collection<String> getNextObjectiveId() {
        return nextObjectiveId;
    }
}
