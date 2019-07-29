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
import com.bigbass.nep.gui.Node.Tier;
import com.bigbass.nep.recipes.IRecipe;
import com.bigbass.nep.recipes.RecipeManager;

public class NodeManager {
	
	private final Stage stage;
	
	private final List<Node> nodes;
	private final List<Node> nodesToRemove;
	
	public NodeManager(Stage stage){
		this.stage = stage;
		
		nodes = new ArrayList<Node>();
		nodesToRemove = new ArrayList<Node>();
	}
	
	public void update(){
		for(Node node : nodes){
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
			nodes.add(node);
			stage.addActor(node.getActor());
		}
	}
	
	public void removeNode(Node node){
		if(node != null){
			node.getActor().remove();
			nodes.remove(node);
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
			final float x = (float) jsonNode.getJsonNumber("x").doubleValue();
			final float y = (float) jsonNode.getJsonNumber("y").doubleValue();
			final int overrideNum = jsonNode.getInt("override", -1);
			Tier override = null;
			if(overrideNum != -1){
				override = Tier.getTierFromNum(overrideNum);
			}
			final int hashCode = jsonNode.getInt("recipeHash", -1);
			
			IRecipe rec = null;
			if(hashCode != -1){
				rec = rm.findRecipe(hashCode);
			}
			
			addNode(new Node(x, y, rec, override));
		}
	}
	
	public void saveNodes(String filename){
		JsonObjectBuilder root = Json.createObjectBuilder();
		
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for(Node node : nodes){
			builder.add(node.toJson());
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
	
	public List<Node> getNodes(){
		return nodes;
	}
}
