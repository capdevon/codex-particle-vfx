package codex.vfx.utils;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public abstract class GeometryControl extends AbstractControl {
    protected Geometry geometry;

    public GeometryControl() {
    }

    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        if (this.spatial == null) {
            this.geometry = null;
        } else {
            if (!(this.spatial instanceof Geometry)) {
                throw new IllegalArgumentException("Control can only control geometry!");
            }

            this.geometry = (Geometry)this.spatial;
        }

    }

    public Geometry getGeometry() {
        return this.geometry;
    }
}

