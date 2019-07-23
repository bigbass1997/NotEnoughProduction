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
}
