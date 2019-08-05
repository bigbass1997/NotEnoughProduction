package com.bigbass.nep.recipes;

public interface IRecipe {
	
	public enum IO {
		INPUT, OUTPUT, BOTH;
	}
	
	public IElement[] getInput();
	public IElement[] getOutput();
	
	public boolean containsElement(String search, IO io);
}
