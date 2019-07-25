package com.bigbass.nep.panel;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigbass.nep.Main;
import com.bigbass.nep.gui.Node;
import com.bigbass.nep.gui.Node.Tier;
import com.bigbass.nep.gui.NodeManager;
import com.bigbass.nep.recipes.IRecipe;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.recipes.RecipeManager.RecipeError;
import com.bigbass.nep.skins.SkinManager;

public class PrimaryPanel extends Panel {

	private final float CAM_SPEED = 400;
	
	private Camera cam;
	private Viewport worldView;
	private Viewport hudView;
	private Stage worldStage;
	private Stage hudStage;
	private ShapeRenderer sr;
	
	private SearchPanel searchPanel;
	
	private Label infoLabel;
	
	private float scalar = 1f;
	
	private final NodeManager nodeManager;
	
	public PrimaryPanel() {
		super();
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 0);
		cam.update();
		
		worldView = new ScreenViewport(cam);
		hudView = new ScreenViewport();
		
		worldStage = new Stage(worldView);
		
		hudStage = new Stage(hudView);
		infoLabel = new Label("", SkinManager.getSkin("fonts/droid-sans-mono.ttf", 10));
		infoLabel.setColor(Color.MAGENTA);
		hudStage.addActor(infoLabel);
		
		sr = new ShapeRenderer(50000);
		sr.setAutoShapeType(true);
		sr.setProjectionMatrix(cam.combined);

		Main.inputMultiplexer.addProcessor(worldStage);
		Main.inputMultiplexer.addProcessor(hudStage);
		Main.inputMultiplexer.addProcessor(new ScrollwheelInputAdapter(){
			@Override
			public boolean scrolled(int amount) {
				if(amount == 1){
					//changeCameraViewport(1);
				} else if(amount == -1){
					//changeCameraViewport(-1);
				}
				return true;
			}
		});
		changeCameraViewport(0);
		
		searchPanel = new SearchPanel(200, 200, hudStage, sr);
		searchPanel.dim.set(500, 500);
		this.panelGroup.panels.add(searchPanel);
		
		
		System.out.println("Loading recipes...");
		RecipeError err = RecipeManager.getInst().loadRecipes("v2.0.7.5-gt-shaped-shapeless");
		System.out.println("Done " + err);
		
		nodeManager = new NodeManager(worldStage);
		
		final List<IRecipe> gtrecs = RecipeManager.getInst().recipes.get("gregtech");
		nodeManager.addNode(new Node(100, 300, gtrecs.get(5)));
		nodeManager.addNode(new Node(400, 300, gtrecs.get(5), Tier.MV));
		nodeManager.addNode(new Node(700, 300, gtrecs.get(5), Tier.EV));
		nodeManager.addNode(new Node(100, 500, gtrecs.get(10215)));
		nodeManager.addNode(new Node(400, 500, gtrecs.get(10215), Tier.MV));
	}
	
	public void render() {
		/*sr.begin(ShapeType.Filled);
		sr.setColor(1, 1, 1, 0.1f);
		sr.rect(Gdx.input.getX(), -Gdx.input.getY() + Gdx.graphics.getHeight(), 250, 10);
		sr.end();*/
		
		//worldStage.getViewport().apply();
		worldStage.draw();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		panelGroup.render();
		
		//hudStage.getViewport().apply();
		hudStage.draw();
		
		/*sr.begin(ShapeType.Filled);
		sr.setColor(Color.FIREBRICK);
		renderDebug(sr);
		sr.end();*/
		
	}
	
	public void update(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			searchPanel.isActive(!searchPanel.isActive());
			searchPanel.isVisible(!searchPanel.isVisible());
		}
		
		nodeManager.update();
		
		worldStage.act(delta);
		
		panelGroup.update(delta);
		
		hudStage.act(delta);
		
		Input input = Gdx.input;
		boolean dirty = false;
		if(input.isKeyPressed(Keys.W)){
			cam.translate(0, CAM_SPEED * delta, 0);
			dirty = true;
		}
		if(input.isKeyPressed(Keys.S)){
			cam.translate(0, -CAM_SPEED * delta, 0);
			dirty = true;
		}
		if(input.isKeyPressed(Keys.A)){
			cam.translate(-CAM_SPEED * delta, 0, 0);
			dirty = true;
		}
		if(input.isKeyPressed(Keys.D)){
			cam.translate(CAM_SPEED * delta, 0, 0);
			dirty = true;
		}
		if(dirty){
			cam.update();
			sr.setProjectionMatrix(cam.combined);
			dirty = false;
		}

		String info = String.format("FPS: %s",
				Gdx.graphics.getFramesPerSecond()
			);
		
		infoLabel.setText(info);
		infoLabel.setPosition(10, Gdx.graphics.getHeight() - (infoLabel.getPrefHeight() / 2) - 5);
	}

	public void resize(int width, int height){
		worldStage.getViewport().update(width, height, false);
		hudStage.getViewport().update(width, height, true);
		sr.setProjectionMatrix(cam.combined);
	}
	
	public boolean isActive() {
		return true; // Always active
	}
	
	public void dispose(){
		worldStage.dispose();
		sr.dispose();
		panelGroup.dispose();
	}
	
	private void changeCameraViewport(int dscalar){
		scalar += dscalar / 10f;
		
		cam.viewportWidth = Gdx.graphics.getWidth() * scalar;
		cam.viewportHeight = Gdx.graphics.getHeight() * scalar;
		cam.update();
		
		sr.setProjectionMatrix(cam.combined);
	}
}
