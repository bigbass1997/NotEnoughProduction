package com.bigbass.nep.recipes;

public interface IRecipe {
	
	public IElement[] getInput();
	public IElement[] getOutput();
	
	public boolean containsElement(String search);
}
