package codex.vfx.test;

import codex.boost.Timer;
import codex.vfx.mesh.MeshPrototype;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.TriParticleGeometry;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.system.AppSettings;

/**
 *
 * @author codex
 */
public class TestPrimitiveParticles extends DemoApplication {

    private ParticleGroup<ParticleData> group;
    private TriParticleGeometry geometry;
    private final Timer timer = new Timer(.1f);
    private final int particlesPerEmission = 5;
    private final float gravity = 5f;

    public static void main(String[] args) {
        TestPrimitiveParticles app = new TestPrimitiveParticles();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    @Override
    public void demoInitApp() {

        group = new ParticleGroup(1);
        rootNode.attachChild(group);

        group.add(new ParticleData());

        geometry = new TriParticleGeometry(group, MeshPrototype.QUAD);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
    }

}
