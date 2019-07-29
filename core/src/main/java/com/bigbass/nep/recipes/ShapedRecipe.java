package com.bigbass.nep.recipes;

import java.util.ArrayList;
import java.util.List;

public class ShapedRecipe implements IRecipe {
	
	public List<Item> itemInputs;
	public Item itemOutput;
	
	public ShapedRecipe(){
		itemInputs = new ArrayList<Item>();
		itemOutput = new Item();
	}

	@Override
	public IElement[] getInput() {
		return itemInputs.toArray(new Item[0]);
	}

	@Override
	public IElement[] getOutput() {
		return new Item[]{itemOutput};
	}

	@Override
	public boolean containsElement(String search) {
		if(itemOutput.localizedName.contains(search) || itemOutput.unlocalizedName.contains(search)){
			return true;
		}
		
		for(Item item : itemInputs){
			if(item.localizedName.contains(search) || item.unlocalizedName.contains(search)){
				return true;
			}
		}
		
		return false;
	}
}
