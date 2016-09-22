package com.cdc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import yml.Box;

public class MyGdxGame extends ApplicationAdapter {
	Texture img;

    static Camera camera;
    static ModelBatch batch;
    static ModelBuilder modelBuilder;
    static Map<String, Box> boxList;

	
	@Override
	public void create () {
		batch = new ModelBatch();
		img = new Texture("badlogic.jpg");
        evaluate(Gdx.files.internal("test.yml").file());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.begin(camera);
		for(Box box:boxList.values())
            batch.render(box.modelInstance);
		batch.end();
	}

    public static void evaluate(File file){
        System.out.println("EVALUATE CALLED!");
        for(Object property:PropertyHandler.getProperties(file)){
            if(property instanceof yml.Camera){
                yml.Camera inCamera = (yml.Camera)property;
                if(camera == null) camera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                if(inCamera.position != null)
                    camera.position.set(Float.parseFloat((String)inCamera.position.get(0)),
                            Float.parseFloat((String)inCamera.position.get(1)),
                            Float.parseFloat((String)inCamera.position.get(2)));
                if(inCamera.lookat != null)
                    camera.lookAt(Float.parseFloat((String)inCamera.lookat.get(0)),
                            Float.parseFloat((String)inCamera.lookat.get(1)),
                            Float.parseFloat((String)inCamera.lookat.get(2)));
                camera.near = 1f;
                camera.far = 300f;
                camera.update();
            }
            else if(property instanceof yml.Box){
                yml.Box inBox = (yml.Box)property;
                if(boxList == null) {
                    boxList = new HashMap<String,Box>();
                }
                if(!boxList.containsKey(inBox.id) ||
                        (boxList.containsKey(inBox.id) && inBox != boxList.get(inBox.id))) {
                    if(boxList.containsKey(inBox.id)) {
                        boxList.get(inBox.id).dispose();
                        boxList.remove(inBox.id);
                    }

                    if (modelBuilder == null) modelBuilder = new ModelBuilder();
                    Model model = modelBuilder.createBox(Float.parseFloat((String)inBox.size.get(0)),
                            Float.parseFloat((String)inBox.size.get(1)),
                            Float.parseFloat((String)inBox.size.get(2)),
                            new Material(ColorAttribute.createDiffuse(Color.RED),
                                    ColorAttribute.createSpecular(1, 1, 1, 1),
                                    FloatAttribute.createShininess(8f)),
                            Usage.Position | Usage.Normal | Usage.TextureCoordinates);
                    ModelInstance instance = new ModelInstance(new ModelInstance(model));
                    instance.transform.setToTranslation(Float.parseFloat((String)inBox.position.get(0)),
                            Float.parseFloat((String)inBox.position.get(1)),
                            Float.parseFloat((String)inBox.position.get(2)));

                    Box newBox = new Box();
                    newBox.model = model;
                    newBox.modelInstance = instance;
                    boxList.put(inBox.id, newBox);
                }
            }
        }
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();

        if(boxList != null) for(Box box:boxList.values()) box.dispose();
	}
}
