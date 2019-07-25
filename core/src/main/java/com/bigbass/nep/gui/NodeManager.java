package com.bigbass.nep.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;

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
	
	public List<Node> getNodes(){
		return nodes;
	}
}
