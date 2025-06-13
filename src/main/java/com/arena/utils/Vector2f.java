package com.arena.utils;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f nor() {
        float len = len();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public Vector2f sub(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float distTo(Vector2f other) {
        float dx = other.x - this.x;
        float dy = other.y - this.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    // Retourne l'angle en degrés entre ce vecteur et un autre
    public float angleDeg(Vector2f other) {
        float dot = this.dot(other);
        float len1 = this.len();
        float len2 = other.len();
        if (len1 == 0 || len2 == 0) return 0f;

        float cos = dot / (len1 * len2);
        cos = Math.max(-1f, Math.min(1f, cos)); // Clamp
        return (float) Math.toDegrees(Math.acos(cos));
    }

    public static Vector2f rotationToDirection(float rotationDegrees) {
        float radians = (float) Math.toRadians(rotationDegrees);
        return new Vector2f((float)Math.sin(radians), (float)Math.cos(radians));
    }


    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
