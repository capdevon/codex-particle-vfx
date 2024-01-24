package codex.vfx.utils;

import jme3utilities.math.noise.Generator;

/**
 *
 * @author codex
 */
public class VfxRandomGenerator extends Generator {

    public float nextFloat(float max) {
        assert max >= 0;
        return nextFloat(0, max);
    }

}