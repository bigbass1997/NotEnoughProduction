package com.bigbass.nep.recipes;

public class ItemProgrammedCircuit extends Item {
	
	public int configNum;
	
	public ItemProgrammedCircuit(){
		super();
	}
	
	public ItemProgrammedCircuit(int amount, String unlocalizedName, String localizedName, int configNum){
		super(amount, unlocalizedName, localizedName);
		
		this.configNum = configNum;
	}
	
	@Override
	public String getLocalizedName() {
		return localizedName + " #" + configNum;
	}
	
	/**
	 * Be cautious when changing this function! Save files depend on this for reverse recipe lookup.
	 */
	@Override
	public int hashCode(){
		return super.hashCode() * configNum;
	}
}
