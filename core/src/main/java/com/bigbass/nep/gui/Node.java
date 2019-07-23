package com.bigbass.nep.gui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bigbass.nep.recipes.IRecipe;
import com.bigbass.nep.skins.SkinManager;

/**
 * Represents a production node, most commonly a machine or a crafting table, with a particular recipe.
 */
public class Node {
	
	public enum Tier {
		ULV(1), LV(2), MV(3), HV(4), EV(5),
		IV(6), LUV(7), ZPM(8), UV(9), UHV(10), UEV(11);
		
		private int tierNum;
		
		private Tier(int t){
			this.tierNum = t;
		}
		
		public int getEUt(){
			return (int) Math.pow(2, (2 * tierNum) + 1);
		}
		
		public int compare(Tier other){
			if(tierNum > other.tierNum){
				return 1;
			} else if(tierNum < other.tierNum){
				return -1;
			} else {
				return 0;
			}
		}
		
		public int getTierNum(){
			return tierNum;
		}
		
		public static Tier getTier(int eut){
			for(int i = 0; i < values().length; i++){
				Tier t = values()[i];
				if(eut < t.getEUt()){
					return t;
				}
			}
			
			return null;
		}
	}
	
	private Table table;
	private IRecipe recipe;
	protected Tier override;
	
	private Vector2 pos;
	
	public Node(float x, float y){
		pos = new Vector2(x, y);
		
		table = new Table(SkinManager.getSkin("fonts/droid-sans-mono.ttf", 10)); // font doesn't really matter here, but skin necessary for other stuff
		recipe = null;
		override = null;
		
		refresh();
	}
	
	public void refresh(){
		NodeTableBuilder.build(this, table);
		table.setPosition(pos.x, pos.y);
	}
	
	public void refresh(IRecipe recipe){
		this.recipe = recipe;
		refresh();
	}
	
	public Actor getActor(){
		return table;
	}
	
	public IRecipe getRecipe(){
		return recipe;
	}

	public void setOverride(Tier tier){
		override = tier;
		this.refresh();
	}
	public Tier getOverride(){
		return override;
	}
}
