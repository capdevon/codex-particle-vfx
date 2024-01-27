package codex.vfx.test.util;

import java.io.File;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.util.SkyFactory;

import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;
import jme3utilities.sky.Updater;

/**
 *
 * @author codex
 */
public abstract class DemoApplication extends SimpleApplication {
    
    private FilterPostProcessor fpp;
    private SkyControl skyControl;
    private boolean lightProbeEnabled = true;
    private boolean shadowsEnabled = true;

    @Override
    public void simpleInitApp() {

        configureCamera();
        setViewDistance(5);
        cam.lookAtDirection(Vector3f.UNIT_XYZ.negate(), Vector3f.UNIT_Y);

        setupScene();
        initLights();
        demoInitApp();
    }

    private void configureCamera() {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustumPerspective(45, aspect, 0.01f, 2000f);
        
        flyCam.setMoveSpeed(7);
        flyCam.setDragToRotate(true);
    }

    public abstract void demoInitApp();

    private void setupScene() {
        Spatial scene = assetManager.loadModel("Demo/demo-scene.j3o");
        rootNode.attachChild(scene);
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    }
    
    private void initLights() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(.01f, .01f, .01f, 1f));
        rootNode.addLight(ambient);
        
        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(new Vector3f(1f, -5f, 1f));
        sunLight.setColor(ColorRGBA.White);
        rootNode.addLight(sunLight);
        
        fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        
        if (shadowsEnabled) {
            DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 4096*2, 4);
            dlsf.setLambda(1);
            dlsf.setShadowCompareMode(CompareMode.Hardware);
            dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
            dlsf.setLight(sunLight);
            fpp.addFilter(dlsf);
        }
        
        if (lightProbeEnabled) {
            Node probeNode = (Node) assetManager.loadModel("Scenes/City_Night_Lights.j3o");
            LightProbe probe = (LightProbe) probeNode.getLocalLightList().iterator().next();
            rootNode.addLight(probe);
        }
        
        Spatial sky = SkyFactory.createSky(assetManager, "Demo/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        rootNode.attachChild(sky);     
        
        skyControl = new SkyControl(assetManager, cam, .5f, StarsOption.TopDome, true);
        rootNode.addControl(skyControl);
        skyControl.setCloudiness(0.8f);
        skyControl.setCloudsYOffset(0.4f);
        skyControl.setTopVerticalAngle(1.784f);
        skyControl.getSunAndStars().setHour(9);
        Updater updater = skyControl.getUpdater();
        updater.setAmbientLight(ambient);
        updater.setMainLight(sunLight);
        updater.setMainMultiplier(.7f);
        skyControl.setEnabled(true);
    }
    
    protected void addFilter(Filter f) {
        fpp.addFilter(f);
    }
    
    public void setLightProbeEnabled(boolean lightProbeEnabled) {
        this.lightProbeEnabled = lightProbeEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    protected void setupVideoCapture(String save) {
        VideoRecorderAppState video = new VideoRecorderAppState();
        video.setFile(new File(System.getProperty("user.home") + save));
        stateManager.attach(video);
    }

    protected void setViewDistance(float distance) {
        cam.setLocation(Vector3f.UNIT_XYZ.mult(distance));
    }

}
