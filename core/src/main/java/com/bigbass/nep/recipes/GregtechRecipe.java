package com.bigbass.nep.recipes;

import java.util.ArrayList;
import java.util.List;

import com.bigbass.nep.gui.nodes.Node.Tier;

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

	@Override
	public boolean containsElement(String search, IO io) {
		if(io == IO.BOTH || io == IO.INPUT){
			for(Item item : itemInputs){
				if(item.localizedName.contains(search) || item.unlocalizedName.contains(search)){
					return true;
				}
			}
			
			for(Fluid fluid : fluidInputs){
				if(fluid.localizedName.contains(search) || fluid.unlocalizedName.contains(search)){
					return true;
				}
			}
		}

		if(io == IO.BOTH || io == IO.OUTPUT){
			for(Item item : itemOutputs){
				if(item.localizedName.contains(search) || item.unlocalizedName.contains(search)){
					return true;
				}
			}
			
			for(Fluid fluid : fluidOutputs){
				if(fluid.localizedName.contains(search) || fluid.unlocalizedName.contains(search)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public long getTotalEU(){
		return ((long) eut) * ((long) duration);
	}
	
	public int getTotalEU(Tier overclock){
		Tier thisTier = Tier.getTierFromEUt(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		int totalEU = (int) (getTotalEU() * Math.pow(2, dif));
		
		return totalEU;
	}
	
	public int getOverclockedEUt(Tier overclock){
		Tier thisTier = Tier.getTierFromEUt(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		return (int) (eut * Math.pow(4, dif));
	}
	
	public int getOverclockedDuration(Tier overclock){
		Tier thisTier = Tier.getTierFromEUt(eut);
		int dif = overclock.getTierNum() - thisTier.getTierNum();
		
		return (int) Math.max(duration / Math.pow(2, dif), 1);
	}
	
	public Tier getTier(){
		return Tier.getTierFromEUt(eut);
	}

	/**
	 * Be cautious when changing this function! Save files depend on this for reverse recipe lookup.
	 */
	@Override
	public int hashCode(){
		int hash = 31 * eut;
		hash += 31 * duration;
		hash += (machineName == null ? 0 : machineName.hashCode());
		hash += itemInputs.hashCode() + itemOutputs.hashCode() + fluidInputs.hashCode() + fluidOutputs.hashCode();
		
		return 7 * hash;
	}
}
