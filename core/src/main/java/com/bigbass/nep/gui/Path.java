package com.bigbass.nep.gui;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import com.bigbass.nep.recipes.IElement;
import com.bigbass.nep.util.Singleton;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.UUID;

public class Path {
	
	public Vector2 sPos; // 's' for start
	public Vector2 ePos; // 'e' for end
	
	private Node begin;
	private Node end;
	private IElement element;

	public Path(){
		this(0, 0, null, 0, 0, null);
	}

	public Path(UUID begin, IElement element, UUID end) {
		this.element = element;

		this.begin = Singleton.getInstance(NodeManager.class).getNode(begin);
		this.end = Singleton.getInstance(NodeManager.class).getNode(end);

		sPos = new Vector2(this.begin.pos.x, this.begin.pos.y);
		ePos = new Vector2(this.end.pos.x, this.end.pos.y);
	}

	public Path(float x1, float y1, Node sNode, float x2, float y2, Node eNode){
		this.begin = sNode;
		this.element = null;
		this.end = eNode;

		sPos = new Vector2(x1, y1);
		ePos = new Vector2(x2, y2);
	}

	public void render(ShapeDrawer drawer) {
		drawer.line(this.begin.pos, this.end.pos, Color.BLACK, 2);
	}
	
	public JsonObject toJson(){
		JsonObjectBuilder root = Json.createObjectBuilder();

		root.add("begin", this.begin.uuid.toString());
		root.add("element", this.element.toString());
		root.add("end", this.end.uuid.toString());

		return root.build();
	}

	static public Path fromJson(JsonObject root) {
		return new Path(
				UUID.fromString(root.getString("begin")),
				null,
				UUID.fromString(root.getString("end"))
		);
	}

	public void setBegin(UUID uuid) {
		this.begin = Singleton.getInstance(NodeManager.class).getNode(uuid);
	}

	public void setEnd(UUID uuid) {
		this.end = Singleton.getInstance(NodeManager.class).getNode(uuid);
	}

	public void setElement(IElement element) {
		this.element = element;
	}

	public Node getBegin() {
		return begin;
	}

	public Node getEnd() {
		return end;
	}

	public IElement getElement() {
		return element;
	}
}
