package codex.vfx.test;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.particles.OverflowStrategy;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.drivers.emission.EmissionPoint;
import codex.vfx.particles.drivers.emission.Emitter;
import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.geometry.TrailingGeometry;
import codex.vfx.particles.tweens.Value;
import codex.vfx.test.util.DemoApplication;

/**
 * 
 * @author codex
 */
public class TestTrailingEffects extends DemoApplication {

    private ParticleGroup<ParticleData> particles;
    private TrailingGeometry geometry;

    public static void main(String[] args) {
        TestTrailingEffects app = new TestTrailingEffects();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    @Override
    public void demoInitApp() {

        particles = new ParticleGroup<>(50);
        particles.setOverflowStrategy(OverflowStrategy.CullOld);
        particles.setVolume(new EmissionPoint());
        Emitter e = Emitter.create();
        e.setEmissionRate(Value.constant(0.01f));
        particles.addDriver(e);
        particles.addDriver(ParticleDriver.TransformToVolume);
        particles.addDriver(new ParticleFactory<ParticleData>() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                p.size.set(.1f);
            }
        });
        rootNode.attachChild(particles);

        geometry = new TrailingGeometry(particles);
        geometry.setFaceCamera(true);
        geometry.setLocalTranslation(0f, 2f, 0f);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.setCullHint(Spatial.CullHint.Never);
        Material mat = new Material(assetManager, "MatDefs/trail.j3md");
        TextureKey texKey = new TextureKey("Textures/wispy-trail.png");
        texKey.setGenerateMips(false);
        Texture tex = assetManager.loadTexture(texKey);
        mat.setTexture("Texture", tex);
        mat.setFloat("Speed", 3.1f);
        mat.setFloat("TextureScale", 3f);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        particles.attachChild(geometry);

        setupCharacter();
        addFilter(new BloomFilter(BloomFilter.GlowMode.Objects));
    }
    
    private void setupCharacter() {
        Spatial model = assetManager.loadModel("Demo/YBot.j3o");
        model.setCullHint(Spatial.CullHint.Never);
        model.setLocalScale(0.01f);
        model.setLocalTranslation(0f, 0f, 0f);
        for (Spatial s : new SceneGraphIterator(model)) {
            if (s instanceof Geometry) {
                Material m = ((Geometry) s).getMaterial();
                m.getAdditionalRenderState().setDepthWrite(true);
                break;
            }
        }
        AnimComposer animComposer = ((Node) model).getChild("Armature").getControl(AnimComposer.class);
        SkinningControl skin = animComposer.getSpatial().getControl(SkinningControl.class);
        animComposer.setCurrentAction("idle");
        rootNode.attachChild(model);
        
        animComposer.setCurrentAction("cold-pistol-kill");
        skin.getAttachmentsNode("mixamorig:LeftHandMiddle1").attachChild(particles);
    }

}
