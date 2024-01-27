package codex.vfx.utils;

import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;

/**
 * Utils for vfx and general applications.
 * 
 * @author codex
 */
public class VfxUtils {

    public static final VfxRandomGenerator gen = new VfxRandomGenerator();
    private static final Plane tempPlane = new Plane();

    /**
     * Generates a random vector within a box with the vectors A and B at opposite
     * corners.
     * 
     * @param a
     * @param b
     * @return random vector within box
     */
    public static Vector3f random(Vector3f a, Vector3f b) {
        return new Vector3f(
                gen.nextFloat(a.x, b.x), 
                gen.nextFloat(a.y, b.y), 
                gen.nextFloat(a.z, b.z));
    }

    /**
     * Constructs a random unit vector with its magnitude between the minimum and
     * maximum distances.
     * 
     * @param minDist
     * @param maxDist
     * @return
     */
    public static Vector3f random(float minDist, float maxDist) {
        return gen.nextUnitVector3f().multLocal(gen.nextFloat(minDist, maxDist));
    }

    /**
     * Generates a random vector within a box.
     * 
     * @param center center point of the box
     * @param x      x radius
     * @param y      y radius
     * @param z      z radius
     * @return
     */
    public static Vector3f random(Vector3f center, float x, float y, float z) {
        return new Vector3f(
                gen.nextFloat(center.x - x, center.x + x), 
                gen.nextFloat(center.y - y, center.y + y),
                gen.nextFloat(center.z - z, center.z + z));
    }

    /**
     * Offsets the vector by an angle and stores the result.
     * <p>
     * Work in progress.
     * 
     * @param vec
     * @param angle
     * @param store stores result, or null
     * @return
     */
    public static Vector3f offsetByAngle(Vector3f vec, float angle, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(vec).normalizeLocal().multLocal(FastMath.cos(angle));
        tempPlane.setOriginNormal(Vector3f.ZERO, store);
        store.set(tempPlane.getClosestPoint(VfxUtils.gen.nextUnitVector3f()
                .multLocal(VfxUtils.gen.nextFloat(FastMath.abs(FastMath.sin(angle)))).addLocal(store)));
        return store.normalizeLocal().multLocal(vec.length());
    }

    /**
     * Returns the average of all vector components.
     * 
     * @param vec
     * @return
     */
    public static float vectorAverage(Vector3f vec) {
        return (vec.x + vec.y + vec.z) / 3;
    }

}
