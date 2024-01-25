package codex.vfx.test;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.filter.KernelFilter;
import codex.vfx.test.util.DemoApplication;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 * Test for kernel post filters.
 * 
 * @author codex
 */
public class TestFilterKernels extends DemoApplication {
    
    // Matrix defining how the kernel samples surrounding pixels.
    // Feel free to play around with this.
    private final Matrix3f kernel = new Matrix3f(
        -2,   -2,  -2,
        -2,   15,  -2,
        -2,   -2,  -2
    );
    
    public static void main(String[] args) {
        TestFilterKernels app = new TestFilterKernels();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    @Override
    public void demoInitApp() {

        setupCharacter();

        FilterPostProcessor f = new FilterPostProcessor(assetManager);
        KernelFilter filter = new KernelFilter();
        filter.setKernelMatrix(kernel);
        filter.setSampleFactor(1f);
        f.addFilter(filter);
        viewPort.addProcessor(f);
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
    }

}
