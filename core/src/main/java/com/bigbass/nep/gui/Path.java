package com.bigbass.nep.gui;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.badlogic.gdx.math.Vector2;

public class Path {
	
	public Vector2 start;
	public Vector2 end;
	
	public Path(){
		this(0, 0, 0, 0);
	}
	
	public Path(float x1, float y1, float x2, float y2){
		start = new Vector2(x1, y1);
		end = new Vector2(x2, y2);
	}
	
	public void render(){
		//TODO
	}
	
	public JsonObject toJson(){ //TODO
		JsonObjectBuilder builder = Json.createObjectBuilder();
		/*builder.add("x", pos.x);
		builder.add("y", pos.y);
		
		if(override != null){
			builder.add("override", override.tierNum);
		}
		
		builder.add("recipeHash", (recipe == null ? -1 : recipe.hashCode()));*/
		
		return builder.build();
	}
}
