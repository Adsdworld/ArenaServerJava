package com.arena.utils;

/**
 * {@link Vector2f}  is a simple class representing a 2D vector with {@code float}  coordinates.
 */
public class Vector2f {
    public float x;
    public float y;

    /**
     * Creates a new {@link Vector2f} .
     *
     * @param x the x coordinate as a {@code float}.
     * @param y the y coordinate as a {@code float}.
     * @implNote This constructor initializes the vector with the given coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds another {@link Vector2f} to this vector and returns a new {@link Vector2f}.
     *
     * @param other the vector to add.
     * @return a new {@link Vector2f} instance representing the sum of this vector and the other {@link Vector2f} .
     * @implNote This method creates a new vector by adding the x and y coordinates of the other vector to this vector's coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Vector2f sub(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    /**
     * Calculates the length of the current {@link Vector2f} .
     *
     * @return the length of the vector as a {@code float} .
     * @implNote This method computes the length using the Pythagorean theorem, which is the square root of the sum of the squares of the x and y coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the euclidian distance to another {@link Vector2f}.
     *
     * @param other the other {@link Vector2f} to which the distance is calculated.
     * @return the distance as a {@code float} .
     * @implNote This method computes the distance using the Pythagorean theorem, which is the square root of the sum of the squares of the differences in x and y coordinates.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public float distTo(Vector2f other) {
        float dx = other.x - this.x;
        float dy = other.y - this.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the dot product of this {@link Vector2f} with another {@link Vector2f}.
     *
     * @param other the other {@link Vector2f} to which the dot product is calculated.
     * @return the dot product as a {@code float} .
     * @implNote This method computes the dot product by multiplying the corresponding x and y coordinates of both vectors and summing the results.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Converts a rotation in degrees to a direction {@link Vector2f} .
     *
     * @param rotationDegrees the rotation in degrees.
     * @return a new {@link Vector2f} representing the direction corresponding to the given rotation.
     * @implNote This method converts the rotation from degrees to radians and then calculates the sine and cosine to create a direction vector.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static Vector2f rotationToDirection(float rotationDegrees) {
        float radians = (float) Math.toRadians(rotationDegrees);
        return new Vector2f((float)Math.sin(radians), (float)Math.cos(radians));
    }

    /**
     * Converts this {@link Vector2f} to a string representation.
     *
     * @implNote This method returns a string that includes the x and y coordinates of the vector.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
