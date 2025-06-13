package com.arena.game.entity;

public abstract class Entity {
    private final String id;
    private final String generalId;

    public Entity(String id) {
        this.id = id;
        this.generalId = id.substring(id.indexOf('_') + 1);
    }

    public String getId() {
        return id;
    }

    public String getGeneralId() {
        return generalId;
    }
}
