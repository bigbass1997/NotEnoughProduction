package com.bigbass.nep.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bigbass.nep.Globals;
import com.bigbass.nep.gui.SearchTableBuilder.ContainerCheckBox;
import com.bigbass.nep.gui.nodes.Node;
import com.bigbass.nep.gui.nodes.NodeManager;
import com.bigbass.nep.gui.nodes.NodeTableBuilder;
import com.bigbass.nep.recipes.IRecipe;
import com.bigbass.nep.recipes.IRecipe.IO;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.recipes.elements.AElement;
import com.bigbass.nep.recipes.processing.Recipe;

public class SearchPane {
	
	private Stage stage;
	private NodeManager nodeManager;
	
	private SearchTableBuilder builder;
	
	private Table table;
	private float tableWidth;
	
	private Hashtable<String, List<Recipe>> filteredRecipes;
	private int currentNodeIndex;
	
	public SearchPane(Stage stage, NodeManager nodeManager){
		this.stage = stage;
		this.nodeManager = nodeManager;
		
		table = new Table();
		table.setVisible(false);
		
		tableWidth = 600;
		
		filteredRecipes = new Hashtable<>();
		
		
		builder = new SearchTableBuilder(this.stage, table, tableWidth);
		builder.build();
		
//		refreshRecipes();
		
		this.stage.addActor(table);
	}
	
	public void refreshRecipes(){
		if(builder.dirtyFilters){
			final RecipeManager rm = RecipeManager.getInst();
			
			final Hashtable<String, List<Recipe>> allRecipes = rm.recipes;
			filteredRecipes.clear();
			filteredRecipes.putAll(allRecipes);
			
			/*for(Actor boxContainer : builder.checkboxes.getChildren().items){
				if(boxContainer instanceof ContainerCheckBox){
					final CheckBox box = ((ContainerCheckBox) boxContainer).box;
					final String boxText = box.getText().toString();
					
					if(box.isChecked()){
						for(String craftingType : RecipeManager.getInst().recipeSources.get(boxText)){
							filteredRecipes.put(craftingType, allRecipes.get(craftingType));
						}
					}
				}
			}*/
			
			// Filter based on process type
			if(builder.searchProcessType != null && !builder.searchProcessType.field.getText().isEmpty()){
				final String type = builder.searchProcessType.field.getText();
				
				final Hashtable<String, List<Recipe>> tmp = new Hashtable<>();
				for(String key : filteredRecipes.keySet()){
					if(key.contains(type) && filteredRecipes.containsKey(key)){
						tmp.put(key, filteredRecipes.get(key));
					}
				}
				
				filteredRecipes = tmp;
			}
			
			// Determine IO filter for next step
			Recipe.IO io = new Recipe.IO(); // remains BOTH if both the checkboxes have the same value
			ContainerCheckBox input = null;
			ContainerCheckBox output = null;
			for(Actor boxContainer : builder.ioCheckboxes.getChildren().items){
				if(boxContainer instanceof ContainerCheckBox){
					final CheckBox box = ((ContainerCheckBox) boxContainer).box;
					
					if(box.getText().toString().equalsIgnoreCase("input")){
						input = (ContainerCheckBox) boxContainer;
					} else if(box.getText().toString().equalsIgnoreCase("output")){
						output = (ContainerCheckBox) boxContainer;
					}
				}
			}
			if(input != null) {
				if(input.box.isChecked()){
					io.input = true;
				}
			}
			if(output != null) {
				if(output.box.isChecked()){
					io.output = true;
				}
			}

			// Filter based on element search name
			final Hashtable<String, List<Recipe>> tmp = new Hashtable<>();
			if(builder.searchName != null && !builder.searchName.field.getText().isEmpty()){
				final String name = builder.searchName.field.getText();
				for (AElement element : AElement.mendeley.values()) {
					if (element.name().contains(name)) {
						if (io.input) {
							for (Recipe rec : element.asInput) {
								if (!tmp.containsKey(rec.group)) {
									tmp.put(rec.group, new ArrayList<>());
								}
								tmp.get(rec.group).add(rec);
							}
						}
						if (io.output) {
							for (Recipe rec : element.asOutput) {
								if (!tmp.containsKey(rec.group)) {
									tmp.put(rec.group, new ArrayList<>());
								}
								tmp.get(rec.group).add(rec);
							}
						}
					}
				}
				
//				for(String key : filteredRecipes.keySet()){
//					for(Recipe recipe : filteredRecipes.get(key)){
//						if(recipe.containsElement(name, io)){
//							if(!tmp.containsKey(key)){
//								tmp.put(key, new ArrayList<>());
//							}
//							tmp.get(key).add(recipe);
//						}
//					}
//				}
//
				filteredRecipes = tmp;
			}
			
			
			String[] keys = filteredRecipes.keySet().toArray(new String[0]);
			Arrays.sort(keys);
			builder.categories.setItems(keys);
			
			if(keys.length > 0){
				builder.categories.setSelectedIndex(0);
				
				builder.scrollPane.layout();
				builder.scrollPane.setScrollPercentY(0);
			}
			
			builder.dirtyNode = true;
			builder.nodeIndexChange = -100;
			builder.dirtyFilters = false;
		}

		if(builder.addNode){
			List<Recipe> selectedRecipes = filteredRecipes.get(builder.categories.getSelected());
			
			Node node = null;
			if(selectedRecipes.size() > 0){
				float x = 0;
				float y = 0;
				if(Globals.primaryCamera != null){
					x = Globals.primaryCamera.position.x;
					y = Globals.primaryCamera.position.y;
				}
				
				node = new Node(x, y, selectedRecipes.get(currentNodeIndex));
			}
			
			if(node != null){
				nodeManager.addNode(node);
				
				builder.addNode = false;
			}
		}
		
		if(builder.dirtyNode){
			if(filteredRecipes != null && builder.categories.getSelected() != null){
				List<Recipe> selectedRecipes = filteredRecipes.get(builder.categories.getSelected());
				if(builder.nodeIndexChange == -100){
					currentNodeIndex = 0;
				} else {
					currentNodeIndex += builder.nodeIndexChange;
					builder.nodeIndexChange = 0;
					
					if(currentNodeIndex < 0){
						currentNodeIndex = selectedRecipes.size() - 1;
					} else if(currentNodeIndex > selectedRecipes.size() - 1){
						currentNodeIndex = 0;
					}
				}
				
				builder.nodeViewText.label.setText("Node Preview (" + (currentNodeIndex + 1) + "/" + selectedRecipes.size() + ")");
				
				Node node = new Node(0, 0);
				if(selectedRecipes.size() > 0) {
					node.refresh(selectedRecipes.get(currentNodeIndex));
				}
				
				NodeTableBuilder.build(node, builder.currentNodeTable, false);
				
				builder.dirtyNode = false;
			}
		}
	}
	
	public void setVisible(boolean val){
		table.setVisible(val);
	}
	public boolean isVisible(){
		return table.isVisible();
	}
	
	public void resize(int width, int height){
		builder.reposition();
	}
	
	public void dispose(){
		builder.dispose();
	}
}
