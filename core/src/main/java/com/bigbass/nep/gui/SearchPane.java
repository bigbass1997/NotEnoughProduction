package com.bigbass.nep.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bigbass.nep.gui.SearchTableBuilder.ContainerCheckBox;
import com.bigbass.nep.recipes.IRecipe;
import com.bigbass.nep.recipes.RecipeManager;

public class SearchPane {
	
	private Stage stage;
	
	private SearchTableBuilder builder;
	
	private Table table;
	private float tableWidth;
	
	public SearchPane(Stage stage){
		this.stage = stage;
		
		table = new Table();
		table.setVisible(false);
		
		tableWidth = 600;
		
		builder = new SearchTableBuilder(this.stage, table, tableWidth);
		builder.build();
		
		refreshRecipes();
		
		this.stage.addActor(table);
	}
	
	public void refreshRecipes(){
		if(builder.dirtyFilters){
			final RecipeManager rm = RecipeManager.getInst();
			
			final Hashtable<String, List<IRecipe>> allRecipes = rm.recipes;
			Hashtable<String, List<IRecipe>> filteredRecipes = new Hashtable<String, List<IRecipe>>();
			
			for(Actor boxContainer : builder.checkboxes.getChildren().items){
				if(boxContainer instanceof ContainerCheckBox){
					final CheckBox box = ((ContainerCheckBox) boxContainer).box;
					final String boxText = box.getText().toString();
					
					if(box.isChecked()){
						for(String craftingType : RecipeManager.getInst().recipeSources.get(boxText)){
							filteredRecipes.put(craftingType, allRecipes.get(craftingType)); // Unknown performance cost
						}
					}
				}
			}
			
			if(builder.searchProcessType != null && !builder.searchProcessType.field.getText().isEmpty()){
				final String type = builder.searchProcessType.field.getText();
				
				final Hashtable<String, List<IRecipe>> tmp = new Hashtable<String, List<IRecipe>>();
				for(String key : filteredRecipes.keySet()){
					if(key.contains(type) && filteredRecipes.containsKey(key)){
						tmp.put(key, filteredRecipes.get(key));
					}
				}
				
				filteredRecipes = tmp;
			}
			
			if(builder.searchName != null && !builder.searchName.field.getText().isEmpty()){
				final String name = builder.searchName.field.getText();
				
				final Hashtable<String, List<IRecipe>> tmp = new Hashtable<String, List<IRecipe>>();
				for(String key : filteredRecipes.keySet()){
					for(IRecipe recipe : filteredRecipes.get(key)){
						if(recipe.containsElement(name)){
							if(!tmp.containsKey(key)){
								tmp.put(key, new ArrayList<IRecipe>());
								
								tmp.get(key).add(recipe);
							}
						}
					}
				}
				
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
			
			builder.dirtyFilters = false;
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
}
