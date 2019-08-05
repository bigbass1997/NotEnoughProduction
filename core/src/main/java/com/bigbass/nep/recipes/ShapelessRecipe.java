package com.bigbass.nep.recipes;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipe implements IRecipe {
	
	public List<Item> itemInputs;
	public Item itemOutput;
	
	public ShapelessRecipe(){
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
	public boolean containsElement(String search, IO io) {
		if((io == IO.BOTH || io == IO.OUTPUT) && (itemOutput.localizedName.contains(search) || itemOutput.unlocalizedName.contains(search))){
			return true;
		}
		
		for(Item item : itemInputs){
			if((io == IO.BOTH || io == IO.INPUT) && (item.localizedName.contains(search) || item.unlocalizedName.contains(search))){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Be cautious when changing this function! Save files depend on this for reverse recipe lookup.
	 */
	@Override
	public int hashCode(){
		return (31 * itemInputs.hashCode()) + (31 * itemOutput.hashCode()) * 7;
	}
}
