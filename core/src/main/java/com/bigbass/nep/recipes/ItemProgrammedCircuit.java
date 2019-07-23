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
}
