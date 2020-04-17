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
import com.bigbass.nep.gui.KeyBindingPane;
import com.bigbass.nep.gui.KeyBindingManager;
import com.bigbass.nep.util.KeyBinding;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.recipes.RecipeManager.RecipeError;
import com.bigbass.nep.skins.SkinManager;
import com.bigbass.nep.util.Singleton;

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

	private Thread loaderThread;

	private float scalar = 1f;
	private boolean loadCompete = false;

	private final NodeManager nodeManager;
	private final PathManager pathManager;

	private KeyBindingManager keyBindings;
	private KeyBindingPane keyBindingPane;

	public PrimaryPanel() {
		super();

		System.out.println("Loading recipes...");
		loaderThread = RecipeManager.getInst().loadRecipesAsync(
				"v2.0.8.4-x0.0.3",
				(RecipeError err) -> {
					System.out.println("Done " + err);
					return null;
				},
				() -> {
					this.loadCompete = true;
					return null;
				}
		);

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

		sr = new ShapeRenderer(50000);
		sr.setAutoShapeType(true);
		sr.setProjectionMatrix(cam.combined);

		nodeManager = Singleton.getInstance(NodeManager.class, worldStage);
		nodeManager.loadNodes("default");

		pathManager = Singleton.getInstance(PathManager.class, worldStage);
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

		this.keyBindings = Singleton.getInstance(KeyBindingManager.class);
		this.keyBindingPane = new KeyBindingPane(this.hudStage, this.keyBindings);

		this.initializeKeyBindings();

		helpLabel = new Label("", SkinManager.getSkin("fonts/droid-sans-mono.ttf", 12));
		helpLabel.setAlignment(Align.bottom);
		helpLabel.setColor(Color.BLACK);
		hudStage.addActor(helpLabel);

		this.keyBindingPane.rebuild();
	}

	private void initializeKeyBindings() {
		this.keyBindings.addBinding(
				"search",
				new KeyBinding()
						.setHelp("open search pane")
						.setCallback(() -> this.toggleSearchPanel())
						.setKeys(
								Keys.F,
								Keys.CONTROL_LEFT
						)
		);
		this.keyBindings.addBinding(
				"bindings",
				new KeyBinding()
						.setHelp("open bindings pane")
						.setCallback(() -> this.toggleKeyBindingPanel())
						.setKeys(
								Keys.CONTROL_LEFT,
								Keys.B
						)
		);
		this.keyBindings.addBinding(
				"save",
				new KeyBinding()
						.setHelp("save nodes and paths")
						.setCallback(() -> this.save())
						.setKeys(
								Keys.CONTROL_LEFT,
								Keys.S
						)
		);
		this.keyBindings.addBinding(
				"unfocus",
				new KeyBinding()
						.setHelp("unfocus all windows")
						.setCallback(() -> this.unfocusAll())
						.setKeys(
								Keys.ESCAPE
						)
		);
		this.keyBindings.addBinding(
				"up",
				new KeyBinding()
						.setHelp("move up")
						.setCallback(() -> this.move(MoveDirection.UP))
						.setKeys(
								Keys.W
						)
						.setHold(true)
		);
		this.keyBindings.addBinding(
				"down",
				new KeyBinding()
						.setHelp("move down")
						.setCallback(() -> this.move(MoveDirection.DOWN))
						.setKeys(
								Keys.S
						)
						.setHold(true)
		);
		this.keyBindings.addBinding(
				"left",
				new KeyBinding()
						.setHelp("move left")
						.setCallback(() -> this.move(MoveDirection.LEFT))
						.setKeys(
								Keys.A
						)
						.setHold(true)
		);
		this.keyBindings.addBinding(
				"right",
				new KeyBinding()
						.setHelp("move right")
						.setCallback(() -> this.move(MoveDirection.RIGHT))
						.setKeys(
								Keys.D
						)
						.setHold(true)
		);
	}

	private Void toggleSearchPanel() {
		searchPane.setVisible(!searchPane.isVisible());

		if(!searchPane.isVisible()){
			Main.inputMultiplexer.addProcessor(worldStage);
		} else {
			Main.inputMultiplexer.removeProcessor(worldStage);
		}
		return null;
	}

	private Void toggleKeyBindingPanel() {
		this.keyBindingPane.setVisible(!this.keyBindingPane.isVisible());

		if(!this.keyBindingPane.isVisible()){
			Main.inputMultiplexer.addProcessor(worldStage);
		} else {
			Main.inputMultiplexer.removeProcessor(worldStage);
		}
		return null;
	}

	private Void save() {
		this.nodeManager.saveNodes("default");
		this.pathManager.savePaths("default-paths");
		return null;
	}

	private Void unfocusAll() {
		this.hudStage.unfocusAll();
		return null;
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
		
		if(
				searchPane.isVisible() || keyBindingPane.isVisible()
		){
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

	private enum MoveDirection {UP, DOWN, LEFT, RIGHT};

	private Void move(MoveDirection direction) {
		boolean dirty = false;
		float delta = Gdx.graphics.getDeltaTime();
		switch (direction) {
			case UP:
				cam.translate(0, CAM_SPEED * delta, 0);
				dirty = true;
				break;
			case DOWN:
				cam.translate(0, -CAM_SPEED * delta, 0);
				dirty = true;
				break;
			case LEFT:
				cam.translate(-CAM_SPEED * delta, 0, 0);
				dirty = true;
				break;
			case RIGHT:
				cam.translate(CAM_SPEED * delta, 0, 0);
				dirty = true;
				break;
		}
		if(dirty){
			cam.update();
			sr.setProjectionMatrix(cam.combined);
			dirty = false;
		}
		return null;
	}
	
	public void update(float delta) {
		if (loaderThread.isAlive()) {
			try {
				loaderThread.join(10);
			} catch (InterruptedException e) {
				System.out.println(String.format("Got exception while loading:\n%s", e));
			}
		}

		Input input = Gdx.input;

		this.keyBindings.act();
		
		nodeManager.update();
		pathManager.update();

		searchPane.refreshRecipes();
		
		worldStage.act(delta);
		
		panelGroup.update(delta);
		
		hudStage.act(delta);
		
		if(!searchPane.isVisible() && !keyBindingPane.isVisible() && input.isButtonPressed(Input.Buttons.MIDDLE)){
			cam.translate(-input.getDeltaX(), input.getDeltaY(), 0);
			cam.update();
			sr.setProjectionMatrix(cam.combined);
		}

		String info = String.format("FPS: %s",
				Gdx.graphics.getFramesPerSecond()
			);
		if (!this.loadCompete) {
			info += ", Loading...";
		}
		
		helpLabel.setText(
				String.format(
						"Press the %s key to open the Recipe Search GUI\nPress the %s key to open hotkey list\nUse the %s, %s, %s, %s keys or the middle mouse button to move around the screen\n%s or closing the program, will save current nodes",
						this.keyBindings.get("search"),
						this.keyBindings.get("bindings"),
						this.keyBindings.get("up"),
						this.keyBindings.get("left"),
						this.keyBindings.get("down"),
						this.keyBindings.get("right"),
						this.keyBindings.get("save")
				)
		);
		infoLabel.setText(info);
		infoLabel.setPosition(10, Gdx.graphics.getHeight() - (infoLabel.getPrefHeight() / 2) - 5);

		helpLabel.setPosition(Gdx.graphics.getWidth() * 0.5f, 24, Align.center);
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
