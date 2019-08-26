package com.bigbass.nep.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bigbass.nep.recipes.RecipeManager;

public class PathManager {
	
	private static PathManager instance;
	
	private final Stage stage;
	
	private List<Path> paths;
	
	private PathManager(Stage stage){
		this.stage = stage;
		
		paths = new ArrayList<Path>();
	}
	
	/**
	 * <p>Retrieves the NodeManager singleton instance.</p>
	 * 
	 * <p><b>WARNING:</b> You must run NodeManager.init() before calling this, or else
	 * this will return null!</p>
	 * 
	 * @return
	 */
	public static PathManager instance(){
		return instance;
	}
	
	public static void init(Stage stage){
		instance = new PathManager(stage);
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
			paths.add(path);
		}
	}
	
	public void removePath(Path path){
		if(path != null){
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
		
		final RecipeManager rm = RecipeManager.getInst();
		for(JsonObject jsonPath : arr.getValuesAs(JsonObject.class)){
			/*final float x = (float) jsonPath.getJsonNumber("x").doubleValue();
			final float y = (float) jsonPath.getJsonNumber("y").doubleValue();
			final int overrideNum = jsonPath.getInt("override", -1);
			Tier override = null;
			if(overrideNum != -1){
				override = Tier.getTierFromNum(overrideNum);
			}
			final int hashCode = jsonPath.getInt("recipeHash", -1);
			
			IRecipe rec = null;
			if(hashCode != -1){
				rec = rm.findRecipe(hashCode);
			}
			
			addPath(new Path(x, y, rec, override));*/
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
}
