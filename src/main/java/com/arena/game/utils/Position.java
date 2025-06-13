package com.arena.game.utils;

import com.arena.utils.Vector3f;

public class Position {
    public Vector3f pos;
    public float rotY;  // rotation autour de Y en degrés (ou radians selon choix)

    public Position(float x, float y, float z, float rotY) {
        this.pos = new Vector3f(x, y, z);
        this.rotY = rotY;
    }

    @Override
    public String toString() {
        return "Position{" + "pos=" + pos + ", rotY=" + rotY + '}';
    }
}
