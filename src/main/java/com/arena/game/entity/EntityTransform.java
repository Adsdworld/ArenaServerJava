package com.arena.game.entity;

public class EntityTransform {
    private float scale;

    public EntityTransform () {
        this.scale = 1.0f;
    }

    public float getScale() {
        return scale;
    }

    /**
     * Sets the scale of the entity in Unity.
     *
     * @param scale the scale factor to set for the entity.
     * @implNote This method sets the scale of the entity in Unity. The scale must be a positive value.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void setScale(float scale) {
        if (scale > 0) {
            this.scale = scale;
        } else {
            throw new IllegalArgumentException("Scale must be positive");
        }
    }
}
