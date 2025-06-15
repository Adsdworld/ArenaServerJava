package com.arena.utils;

/**
 * Vector3f is a simple class representing a 3D vector with float coordinates.
 */
public class Vector3f {
    public float x, y, z;

    /**
     * Creates a new Vector3f.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @implNote This constructor initializes the vector with the given coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
