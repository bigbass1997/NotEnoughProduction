package com.bigbass.nep.gui;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Path {
	
	public Vector2 sPos; // 's' for start
	public Vector2 ePos; // 'e' for end
	
	/** May be null */
	public Node sNode;
	/** May be null */
	public Node eNode;
	
	public Path(){
		this(0, 0, null, 0, 0, null);
	}
	
	public Path(float x1, float y1, Node sNode, float x2, float y2, Node eNode){
		sPos = new Vector2(x1, y1);
		this.sNode = sNode;
		
		ePos = new Vector2(x2, y2);
		this.eNode = eNode;
	}
	
	public void render(ShapeDrawer drawer){
		drawer.line(sPos, ePos, Color.BLACK, 2);
	}
	
	public JsonObject toJson(){
		JsonObjectBuilder root = Json.createObjectBuilder();
		
		JsonObjectBuilder start = Json.createObjectBuilder();
		start.add("x", sPos.x);
		start.add("y", sPos.y);
		if(sNode != null){
			start.add("nodeX", sNode.pos.x);
			start.add("nodeY", sNode.pos.y);
		}
		root.add("start", start);
		
		JsonObjectBuilder end = Json.createObjectBuilder();
		end.add("x", ePos.x);
		end.add("y", ePos.y);
		if(eNode != null){
			end.add("nodeX", eNode.pos.x);
			end.add("nodeY", eNode.pos.y);
		}
		root.add("end", end);
		
		return root.build();
	}
}
