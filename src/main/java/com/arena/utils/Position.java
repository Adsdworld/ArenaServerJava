package com.arena.utils;

public class Position {
    public Vector3f pos;
    public float rotY;  // rotation autour de Y en degrés (ou radians selon choix)

    public Position(float x, float y, float z, float rotY) {
        this.pos = new Vector3f(x, y, z);
        this.rotY = rotY;
    }

    public Position(Vector3f pos, float rotY) {
        this.pos = pos;
        this.rotY = rotY;
    }

    @Override
    public String toString() {
        return "Position{" + "pos=" + pos + ", rotY=" + rotY + '}';
    }
}
