package com.arena.game.entity;

public class EntityTransform {
    private float scale;

    public EntityTransform () {
        this.scale = 1.0f;
    }

    public float getScale() {
        return scale;
    }
    public void setScale(float scale) {
        if (scale > 0) {
            this.scale = scale;
        } else {
            throw new IllegalArgumentException("Scale must be positive");
        }
    }
}
