package com.bigbass.nep.panel;

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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigbass.nep.Main;
import com.bigbass.nep.gui.listeners.ScrollwheelInputAdapter;
import com.bigbass.nep.gui.NodeManager;
import com.bigbass.nep.gui.SearchPane;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.recipes.RecipeManager.RecipeError;
import com.bigbass.nep.skins.SkinManager;

public class PrimaryPanel extends Panel {

	private final float CAM_SPEED = 400;
	
	private final Color COLOR_GRID = new Color(0xDDDDDD88);
	
	private Camera cam;
	private Viewport worldView;
	private Viewport hudView;
	private Stage worldStage;
	private Stage hudStage;
	private ShapeRenderer sr;
	
	private SearchPane searchPane;
	
	private Label infoLabel;
	private Label helpLabel;
	
	private float scalar = 1f;
	
	private final NodeManager nodeManager;
	
	public PrimaryPanel() {
		super();

		System.out.println("Loading recipes...");
		RecipeError err = RecipeManager.getInst().loadRecipes("v2.0.7.5-gt-shaped-shapeless");
		System.out.println("Done " + err);
		
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
		
		helpLabel = new Label("Press the F1 key to open the Recipe Search GUI\nUse the WASD keys to move around the screen", SkinManager.getSkin("fonts/droid-sans-mono.ttf", 12));
		helpLabel.setAlignment(Align.center);
		helpLabel.setColor(Color.CYAN);
		hudStage.addActor(helpLabel);
		
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
		
		nodeManager = new NodeManager(worldStage);
		
		/*final List<IRecipe> gtrecs = RecipeManager.getInst().recipes.get("Compressor");
		nodeManager.addNode(new Node(100, 300, gtrecs.get(5)));
		nodeManager.addNode(new Node(400, 300, gtrecs.get(5), Tier.MV));
		nodeManager.addNode(new Node(700, 300, gtrecs.get(5), Tier.EV));
		nodeManager.addNode(new Node(100, 500, gtrecs.get(562)));
		nodeManager.addNode(new Node(400, 500, gtrecs.get(562), Tier.MV));*/

		searchPane = new SearchPane(hudStage, nodeManager);
		
		
		cam.translate(-cam.viewportWidth * 0.2f, -cam.viewportHeight * 0.2f, 0);
		cam.update();
	}
	
	public void render() {
		sr.begin(ShapeType.Line);

		sr.setColor(COLOR_GRID);
		for(int x = -500; x < 500; x++){
			sr.line(x * 50, -50000, x * 50, 50000);
		}
		for(int y = -500; y < 500; y++){
			sr.line(-50000, y * 50, 50000, y * 50);
		}
		
		sr.setColor(1, 0, 0, 1);
		sr.line(0, -50000, 0, 50000);
		sr.setColor(0, 1, 0, 1);
		sr.line(-50000, 0, 50000, 0);
		sr.end();

		panelGroup.render();
		
		//worldStage.getViewport().apply();
		worldStage.draw();
		
		if(searchPane.isVisible()){
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			sr.begin(ShapeType.Filled);
			sr.setColor(0.2f, 0.2f, 0.2f, 0.7f);
			sr.rect(cam.position.x - (cam.viewportWidth * 0.5f), cam.position.y - (cam.viewportHeight * 0.5f), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			sr.end();
		}

		//hudStage.getViewport().apply();
		hudStage.draw();
		
		
		/*sr.begin(ShapeType.Filled);
		sr.setColor(Color.FIREBRICK);
		renderDebug(sr);
		sr.end();*/
		
	}
	
	public void update(float delta) {
		Input input = Gdx.input;
		
		if(input.isKeyJustPressed(Keys.F1)){
			searchPane.setVisible(!searchPane.isVisible());
			
			if(!searchPane.isVisible()){
				Main.inputMultiplexer.addProcessor(worldStage);
			} else {
				Main.inputMultiplexer.removeProcessor(worldStage);
			}
		}
		
		if(input.isKeyJustPressed(Keys.ESCAPE)){
			hudStage.unfocusAll();
		}
		
		nodeManager.update();
		
		searchPane.refreshRecipes();
		
		worldStage.act(delta);
		
		panelGroup.update(delta);
		
		hudStage.act(delta);
		
		if(!searchPane.isVisible()){
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
		}

		String info = String.format("FPS: %s",
				Gdx.graphics.getFramesPerSecond()
			);
		
		infoLabel.setText(info);
		infoLabel.setPosition(10, Gdx.graphics.getHeight() - (infoLabel.getPrefHeight() / 2) - 5);

		helpLabel.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() - 15, Align.center);
	}

	public void resize(int width, int height){
		searchPane.resize(width, height);
		
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
		searchPane.dispose();
	}
	
	private void changeCameraViewport(int dscalar){
		scalar += dscalar / 10f;
		
		cam.viewportWidth = Gdx.graphics.getWidth() * scalar;
		cam.viewportHeight = Gdx.graphics.getHeight() * scalar;
		cam.update();
		
		sr.setProjectionMatrix(cam.combined);
	}
}
