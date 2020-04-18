package com.bigbass.nep.recipes;

public class Fluid implements IElement {
	
	public int amount;
	public String unlocalizedName;
	public String localizedName;

	public Fluid(){}
	
	public Fluid(int amount, String unlocalizedName, String localizedName){
		this.amount = amount;
		this.unlocalizedName = unlocalizedName;
		this.localizedName = localizedName;
	}

	@Override
	public String getName() {
		return this.unlocalizedName;
	}

	@Override
	public String toString(){
		return String.format("{%dL %s}", amount, localizedName);
	}

	@Override
	public int getAmount() {
		return amount;
	}
	
	@Override
	public String getLocalizedName() {
		return localizedName;
	}

	/**
	 * Be cautious when changing this function! Save files depend on this for reverse recipe lookup.
	 */
	@Override
	public int hashCode(){
		int hash = 31 * amount;
		hash += unlocalizedName.hashCode();
		hash += localizedName.hashCode();
		
		return 7 * hash;
	}
}
