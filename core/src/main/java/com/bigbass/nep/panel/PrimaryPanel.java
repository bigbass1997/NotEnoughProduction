package com.bigbass.nep.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
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
import com.bigbass.nep.Globals;
import com.bigbass.nep.Main;
import com.bigbass.nep.gui.listeners.ScrollwheelInputAdapter;
import com.bigbass.nep.gui.NodeManager;
import com.bigbass.nep.gui.PathManager;
import com.bigbass.nep.gui.SearchPane;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.recipes.RecipeManager.RecipeError;
import com.bigbass.nep.skins.SkinManager;

public class PrimaryPanel extends Panel {

	private final float CAM_SPEED = 400;
	
	private final Color COLOR_GRID = new Color(0xBBBBBB59);
	private final Color BACKGROUND_COLOR = new Color(0xDFDFDFFF);
	
	private OrthographicCamera cam;
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
	private final PathManager pathManager;
	
	public PrimaryPanel() {
		super();

		System.out.println("Loading recipes...");
		RecipeError err = RecipeManager.getInst().loadRecipes("v2.0.7.5-gt-shaped-shapeless");
		System.out.println("Done " + err);
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 0);
		cam.update();
		
		Globals.primaryCamera = cam;
		
		worldView = new ScreenViewport(cam);
		hudView = new ScreenViewport();
		
		worldStage = new Stage(worldView);
		
		hudStage = new Stage(hudView);
		infoLabel = new Label("", SkinManager.getSkin("fonts/droid-sans-mono.ttf", 10));
		infoLabel.setColor(Color.MAGENTA);
		hudStage.addActor(infoLabel);
		
		helpLabel = new Label("Press the F1 key to open the Recipe Search GUI\nUse the WASD keys or the middle mouse button to move around the screen\nCTRL+S or closing the program, will save current nodes", SkinManager.getSkin("fonts/droid-sans-mono.ttf", 12));
		helpLabel.setAlignment(Align.center);
		helpLabel.setColor(Color.BLACK);
		hudStage.addActor(helpLabel);
		
		sr = new ShapeRenderer(50000);
		sr.setAutoShapeType(true);
		sr.setProjectionMatrix(cam.combined);
		
		NodeManager.init(worldStage);
		nodeManager = NodeManager.instance();
		nodeManager.loadNodes("default");

		PathManager.init(worldStage);
		pathManager = PathManager.instance();
		pathManager.loadPaths("default-paths");
		
		searchPane = new SearchPane(hudStage, nodeManager);
		
		cam.translate(-cam.viewportWidth * 0.2f, -cam.viewportHeight * 0.2f, 0);
		cam.update();

		Main.inputMultiplexer.addProcessor(new ScrollwheelInputAdapter(){
			@Override
			public boolean scrolled(int amount) {
				if(searchPane.isVisible()){
					return false;
				}
				
				if(amount == 1){
					changeCameraViewport(1);
				} else if(amount == -1){
					changeCameraViewport(-1);
				}
				return false;
			}
		});
		changeCameraViewport(0);
		
		Main.inputMultiplexer.addProcessor(worldStage);
		Main.inputMultiplexer.addProcessor(hudStage);
	}
	
	public void render() {
		sr.begin(ShapeType.Filled);
		
		sr.setColor(BACKGROUND_COLOR);
		sr.rect(cam.position.x - (cam.viewportWidth * 0.5f * cam.zoom), cam.position.y - (cam.viewportHeight * 0.5f * cam.zoom), Gdx.graphics.getWidth() * cam.zoom, Gdx.graphics.getHeight() * cam.zoom);
		
		sr.set(ShapeType.Line);

		sr.setColor(COLOR_GRID);
		final int GAP = 20;
		for(int x = -500; x < 500; x++){
			sr.line(x * GAP, -50000, x * GAP, 50000);
		}
		for(int y = -500; y < 500; y++){
			sr.line(-50000, y * GAP, 50000, y * GAP);
		}
		
		sr.setColor(1, 0, 0, 1);
		sr.line(0, -50000, 0, 50000);
		sr.setColor(0, 1, 0, 1);
		sr.line(-50000, 0, 50000, 0);
		sr.end();
		
		pathManager.render();

		panelGroup.render();
		
		//worldStage.getViewport().apply();
		worldStage.draw();
		
		if(searchPane.isVisible()){
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			sr.begin(ShapeType.Filled);
			sr.setColor(0.2f, 0.2f, 0.2f, 0.7f);
			sr.rect(cam.position.x - (cam.viewportWidth * 0.5f * cam.zoom), cam.position.y - (cam.viewportHeight * 0.5f * cam.zoom), Gdx.graphics.getWidth() * cam.zoom, Gdx.graphics.getHeight() * cam.zoom);
			sr.end();
		}

		//hudStage.getViewport().apply();
		hudStage.draw();
	}
	
	public void update(float delta) {
		Input input = Gdx.input;
		
		if(input.isKeyPressed(Keys.CONTROL_LEFT) && input.isKeyJustPressed(Keys.S)){
			nodeManager.saveNodes("default");
			pathManager.savePaths("default-paths");
		}
		
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
		pathManager.update();
		
		searchPane.refreshRecipes();
		
		worldStage.act(delta);
		
		panelGroup.update(delta);
		
		hudStage.act(delta);
		
		if(!searchPane.isVisible() && !input.isKeyPressed(Keys.CONTROL_LEFT)){
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
		
		if(!searchPane.isVisible() && input.isButtonPressed(Input.Buttons.MIDDLE)){
			cam.translate(-input.getDeltaX(), input.getDeltaY(), 0);
			cam.update();
			sr.setProjectionMatrix(cam.combined);
		}

		String info = String.format("FPS: %s",
				Gdx.graphics.getFramesPerSecond()
			);
		
		infoLabel.setText(info);
		infoLabel.setPosition(10, Gdx.graphics.getHeight() - (infoLabel.getPrefHeight() / 2) - 5);

		helpLabel.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() - 24, Align.center);
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
		nodeManager.saveNodes("default"); //TODO Change these saves into a single workspace save file
		pathManager.savePaths("default-paths");
		pathManager.dispose();
		
		worldStage.dispose();
		sr.dispose();
		panelGroup.dispose();
		searchPane.dispose();
	}
	
	private void changeCameraViewport(int dscalar){
		if((scalar + (dscalar / 10f)) >= 0){
			scalar += dscalar / 10f;
		}
		
		cam.zoom = scalar;
		
		cam.update();

		worldStage.getViewport().apply();
		hudStage.getViewport().apply();
		
		sr.setProjectionMatrix(cam.combined);
	}
}
