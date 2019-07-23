package com.bigbass.nep.recipes;

import java.util.ArrayList;
import java.util.List;

import com.bigbass.nep.gui.Node.Tier;

public class GregtechRecipe implements IRecipe {
	
	public boolean enabled;
	public int duration;
	public int eut;
	public String machineName;
	public List<Item> itemInputs;
	public List<Item> itemOutputs;
	public List<Fluid> fluidInputs;
	public List<Fluid> fluidOutputs;
	
	public GregtechRecipe(){
		itemInputs = new ArrayList<Item>();
		itemOutputs = new ArrayList<Item>();
		fluidInputs = new ArrayList<Fluid>();
		fluidOutputs = new ArrayList<Fluid>();
	}

	@Override
	public IElement[] getInput() {
		IElement[] arr = new IElement[itemInputs.size() + fluidInputs.size()];
		
		// Apache Lang ArrayUtils could be used instead, but that creates more garbage
		int i = 0;
		for(Item item : itemInputs){
			arr[i] = item;
			i++;
		}
		for(Fluid fluid : fluidInputs){
			arr[i] = fluid;
			i++;
		}
		
		return arr;
	}

	@Override
	public IElement[] getOutput() {
		IElement[] arr = new IElement[itemOutputs.size() + fluidOutputs.size()];
		
		// Apache Lang ArrayUtils could be used instead, but that creates more garbage
		int i = 0;
		for(Item item : itemOutputs){
			arr[i] = item;
			i++;
		}
		for(Fluid fluid : fluidOutputs){
			arr[i] = fluid;
			i++;
		}
		
		return arr;
	}
	
	public int getTotalEU(){
		return eut * duration;
	}
	
	public int getTotalEU(Tier overclock){
		Tier thisTier = Tier.getTier(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		int totalEU = (int) (getTotalEU() * Math.pow(2, dif));
		
		return totalEU;
	}
	
	public int getOverclockedEUt(Tier overclock){
		Tier thisTier = Tier.getTier(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		return (int) (eut * Math.pow(4, dif));
	}
	
	public int getOverclockedDuration(Tier overclock){
		Tier thisTier = Tier.getTier(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		return (int) Math.max(duration / Math.pow(2, dif), 1);
	}
	
	public Tier getTier(){
		return Tier.getTier(eut);
	}
}
