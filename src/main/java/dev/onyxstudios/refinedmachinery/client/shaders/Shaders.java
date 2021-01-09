package dev.onyxstudios.refinedmachinery.client.shaders;

public class Shaders {

    public static SettingsSceneShader SCENE_SHADER = new SettingsSceneShader();
    public static BoundingBoxShader BB_SHADER = new BoundingBoxShader();

    public static void init() {
        SCENE_SHADER.init();
        BB_SHADER.init();
    }
}
