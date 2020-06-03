
/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.util;

import com.karuslabs.annotations.Static;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


/**
 * This class contains of static methods to manipulate vectors and other similar 
 * structures.
 * 
 * @author EffectLib - Slikey
 */
public @Static class Vectors {
    
    /**
     * A reduction operation that reduces the given coordinates to a {@code container}.
     * 
     * @param <T> the type of the {@code container}
     */
    @FunctionalInterface
    static interface Reduction<T> {
        
        /**
         * Reduces the coordinates to the {@code container}.
         * 
         * @param container the container to which the coordinates are reduced
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param z the Z coordinate
         * @return the {@code container}
         */
        public T reduce(T type, double x, double y, double z);
        
    }
    
    private static Reduction<Vector> VECTOR = (vector, x, y, z) -> vector.setX(x).setY(y).setZ(z);
        
    private static Reduction<Location> LOCATION = (location, x, y, z) -> {
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        return location;
    };

    
    /**
     * Rotates the vector about the axes using the given radian angles.
     * 
     * @param vector the vector
     * @param angleX the radian angle to rotate the angle around the X axis
     * @param angleY the radian angle to rotate the angle around the Y axis
     * @param angleZ the radian angle to rotate the angle around the Z axis
     * @return the given vector
     */
    public static Vector rotate(Vector vector, double angleX, double angleY, double angleZ) {
        rotateAroundXAxis(vector, angleX);
        rotateAroundYAxis(vector, angleY);
        rotateAroundZAxis(vector, angleZ);
        
        return vector;
    }
    
    /**
     * Rotates the vector about the X axis by the given radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the given vector
     */
    public static Vector rotateAroundXAxis(Vector vector, double angle) {
        double cos = cos(angle);
        double sin = sin(angle);
        double y = vector.getY() * cos - vector.getZ() * sin;
        double z = vector.getY() * sin + vector.getZ() * cos;
        
        return vector.setY(y).setZ(z);
    }
    
    /**
     * Rotates the vector rotated about the Y axis by the given radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the given vector
     */
    public static Vector rotateAroundYAxis(Vector vector, double angle) {
        double cos = cos(angle);
        double sin = sin(angle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        
        return vector.setX(x).setZ(z);
    }
    
    /**
     * Rotates the vector about the Z axis by the given radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the given vector
     */
    public static Vector rotateAroundZAxis(Vector vector, double angle) {
        double cos = cos(angle);
        double sin = sin(angle);
        double x = vector.getX() * cos - vector.getY() * sin;
        double y = vector.getX() * sin + vector.getY() * cos;
        
        return vector.setX(x).setY(y);
    }
    
    
    /**
     * Rotates the vector rotated about the given pivot.
     * 
     * @param vector the vector
     * @param pivot the pivot which the vector is rotated about
     * @return the given vector
     */
    public static Vector rotate(Vector vector, Location pivot) {
        return rotate(vector, pivot.getYaw(), pivot.getPitch());
    }
    
    /**
     * Rotates the vector about the given yaw and pitch.
     * 
     * @param vector the vector
     * @param yawDegrees the yaw in degrees
     * @param pitchDegrees the pitch in degrees
     * @return the given vector
     */
    public static Vector rotate(Vector vector, float yawDegrees, float pitchDegrees) {
        return rotate(VECTOR, vector, vector.getX(), vector.getY(), vector.getZ(), yawDegrees, pitchDegrees);
    }
    
    
    /**
     * Rotates the location about the given pivot.
     * 
     * @param location the location
     * @param pivot the pivot which the location is rotated about
     * @return the given location
     */
    public static Location rotate(Location location, Location pivot) {
        return rotate(location, pivot.getYaw(), pivot.getPitch());
    }
    
    /**
     * Rotates the location about the given yaw and pitch.
     * 
     * @param location the location
     * @param yawDegrees the yaw in degrees
     * @param pitchDegrees the pitch in degrees
     * @return the given location
     */
    public static Location rotate(Location location, float yawDegrees, float pitchDegrees) {
        return rotate(LOCATION, location, location.getX(), location.getY(), location.getZ(), yawDegrees, pitchDegrees);
    }
    
    
    /**
     * Rotates the x, y and z coordinates about the given yaw and pitch and reduces
     * the resultant coordinates to the given container.
     * 
     * @param <T> the type of the container
     * @param reducer the reducing function
     * @param container the container
     * @param initialX the X coordinate
     * @param initialY the Y coordinate
     * @param initialZ the Z coordinate
     * @param yawDegrees the yaw in degrees
     * @param pitchDegrees the pitch in degrees
     * @return the given location
     */
    static <T> T rotate(Reduction<T> reducer, T container, double initialX, double initialY, double initialZ, float yawDegrees, float pitchDegrees) {
        double yaw = toRadians(-(yawDegrees + 90));
        double pitch = toRadians(-pitchDegrees);


        // Y axis rotation (Yaw)
        double cosPitch = cos(pitch);
        double sinPitch = sin(pitch);
        
        double x = initialX * cosPitch - initialY * sinPitch;
        double y = initialX * sinPitch + initialY * cosPitch;

        // Z axis rotation (Pitch) 
        double cosYaw = cos(yaw);
        double sinYaw = sin(yaw);
        
        initialX = x;
        x = initialZ * sinYaw + initialX * cosYaw;
        double z = initialZ * cosYaw - initialX * sinYaw;

        return reducer.reduce(container, x, y, z);
    }

    
    /**
     * Returns the angle <i>theta</i> from the conversion of the given vector coordinates 
     * ({@code x} and {@code y}) to polar coordinates (r,&nbsp;<i>theta</i>).
     * 
     * 
     * @param vector the vector to rotate
     * @return the angle <i>theta</i>
     */
    // To be honest, my math sucks and I have absolutely no clue what this method does.
    public static double angleToXAxis(Vector vector) {
        return atan2(vector.getX(), vector.getY());
    }
    
}
