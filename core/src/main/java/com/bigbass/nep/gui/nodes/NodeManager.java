package com.bigbass.nep.gui.nodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bigbass.nep.gui.Path;
import com.bigbass.nep.gui.PathManager;
import com.bigbass.nep.recipes.RecipeManager;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class NodeManager {
	private final Stage stage;
	
	private final Map<UUID, Node> nodes;
	private final List<Node> nodesToRemove;

	private final TextureRegion WHITE1X1;
	
	public final ShapeDrawer shapeDrawer;

	private PathManager pathManager = null;
	
	public NodeManager(Stage stage){
		this.stage = stage;
		
		nodes = new HashMap<>();
		nodesToRemove = new ArrayList<Node>();
		
		WHITE1X1 = new TextureRegion(new Texture(Gdx.files.internal("textures/white1x1.png")));
		
		shapeDrawer = new ShapeDrawer(stage.getBatch(), WHITE1X1);
	}

	public void setPathManager(PathManager pathManager) {
		this.pathManager = pathManager;
	}

	public void update(){
		for(Node node : nodes.values()){
			if(node.shouldRemove()){
				nodesToRemove.add(node);
			}
		}
		
		for(Node node : nodesToRemove){
			removeNode(node);
		}
		nodesToRemove.clear();
	}
	
	public void addNode(Node node){
		if(node != null){
			nodes.put(node.uuid, node);
			stage.addActor(node.getActor());
		}
	}
	
	public void removeNode(Node node){
		if(node != null){
			node.getActor().remove();
			for (List<Path> inputs : node.inputs.values()) {
				for (Path path : inputs) {
					this.pathManager.removePath(path);
				}
			}

			for (Path input : node.outputs.values()) {
				this.pathManager.removePath(input);
			}
			nodes.remove(node.uuid);
		}
	}
	
	public void loadNodes(String filename){
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
		
		final JsonArray arr = root.getJsonArray("nodes");
		if(arr == null){
			return;
		}
		
		final RecipeManager rm = RecipeManager.getInst();
		for(JsonObject jsonNode : arr.getValuesAs(JsonObject.class)){
			addNode(Node.fromJson(jsonNode));
		}
	}
	
	public void saveNodes(String filename){
		JsonObjectBuilder root = Json.createObjectBuilder();
		
		JsonArrayBuilder builder = Json.createArrayBuilder();
		
		boolean errorFound = false;
		for(Node node : nodes.values()){
			builder.add(node.toJson());
			
			if(node.getRecipe() == null){
				errorFound = true;
			}
		}
		if(errorFound){
			filename = filename + "-error";
			System.out.println("WARNING: One or more nodes had no valid recipe, this is likely a result of a corrupted or nonexistent recipe atlas. To protect your data, a different filename will be used.");
		}
		
		root.add("nodes", builder.build());
		
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

	public Node getNode(UUID uuid) {
		return nodes.get(uuid);
	}
	
	public void dispose(){
		WHITE1X1.getTexture().dispose();
	}
}
