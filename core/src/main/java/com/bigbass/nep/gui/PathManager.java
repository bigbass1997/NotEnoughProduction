package com.bigbass.nep.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.bigbass.nep.recipes.IElement;
import com.bigbass.nep.util.Singleton;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PathManager {
	public void buildSetNode(UUID uuid, IElement element, boolean input) {
		if (this.builderPath.getElement() == null) {
			this.builderPath.setElement(element);
		} else if (!this.builderPath.getElement().getName().equals(element.getName())) {
			return;
		}
		if (input) {
			this.builderPath.setEnd(uuid);
		} else {
			Path path = Singleton.getInstance(NodeManager.class).getNode(uuid).outputs.get(element.getName());
			if (path != null) {
				Singleton.getInstance(PathManager.class).removePath(path);
			}
			this.builderPath.setBegin(uuid);
		}
	}

	public boolean buildComplete() {
		return this.builderPath.getBegin() != null && this.builderPath.getEnd() != null;
	}

	private Path builderPath;

	private final ShapeDrawer drawer;
	private final TextureRegion white1x1;


	private List<Path> paths;
	
	public PathManager(Stage stage){
		builderPath = new Path();
		white1x1 = new TextureRegion(new Texture(Gdx.files.internal("textures/white1x1.png")));
		drawer = new ShapeDrawer(stage.getBatch(), white1x1);
		
		paths = new ArrayList<Path>();
	}

	public void render(){
		drawer.getBatch().begin();
		for(Path path : paths){
			path.render(drawer);
		}
		drawer.getBatch().end();
	}
	
	public void update(){
		/*for(Path path : paths){
			if(path.shouldRemove()){
				pathsToRemove.add(path);
			}
		}
		
		for(Path path : pathsToRemove){
			removePath(path);
		}
		pathsToRemove.clear();*/
	}
	
	public void addPath(Path path){
		if(path != null){
			path.getBegin().outputs.put(path.getElement().getName(), path);
			path.getEnd().inputs.get(path.getElement().getName()).add(path);
			this.paths.add(path);
		}
	}
	
	public void removePath(Path path){
		if(path != null){
			path.getBegin().outputs.remove(path.getElement().getName());
			path.getEnd().inputs.get(path.getElement().getName()).remove(path);  // TODO (rebenkoy) Hash map for this?
			paths.remove(path);
		}
	}
	
	public void loadPaths(String filename){
		if(filename == null || filename.trim().isEmpty()){
			return;
		}
		
		filename = filename.trim().replace(".json", "");
		FileHandle handle = Gdx.files.local("saves/" + filename + ".json");
		
		if(!handle.exists()){
			return;
		}
		
		JsonObject root;
		
		try {
			JsonReader reader = Json.createReader(new FileReader(handle.file()));
			
			root = reader.readObject();
			
			reader.close();
		} catch (FileNotFoundException | JsonException | IllegalStateException e){
			e.printStackTrace();
			return;
		}
		
		if(root == null){
			return;
		}
		
		final JsonArray arr = root.getJsonArray("paths");
		if(arr == null){
			return;
		}
		
		for(JsonObject jsonPath : arr.getValuesAs(JsonObject.class)){
			this.addPath(Path.fromJson(jsonPath));
		}
	}
	
	public void savePaths(String filename){
		JsonObjectBuilder root = Json.createObjectBuilder();
		
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for(Path path : paths){
			builder.add(path.toJson());
		}
		
		root.add("paths", builder.build());
		
		FileHandle dir = Gdx.files.local("saves/");
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		filename = filename.trim().replace(".json", "");
		FileHandle handle = Gdx.files.local("saves/" + filename + ".json");
		
		String json = root.build().toString();
		
		try {
			FileWriter writer = new FileWriter(handle.file());
			writer.write(json);
			writer.close();
			
			System.out.println(filename + " saved.");
		} catch (IOException | NullPointerException e) {
			System.out.println(filename + " failed to save!");
			e.printStackTrace();
		}
	}
	
	public List<Path> getPaths(){
		return paths;
	}
	
	public void dispose(){
		white1x1.getTexture().dispose();
	}

	public void createPath(UUID node_id, IElement element, boolean input, InputEvent event, float x, float y) {
		this.buildSetNode(node_id, element, input);
		if (this.buildComplete()) {
			this.addPath(this.builderPath);
			this.builderPath = new Path();
		}
	}
}
