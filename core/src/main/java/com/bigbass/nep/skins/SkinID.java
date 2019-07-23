package com.bigbass.nep.skins;

public class SkinID {
	
	public String fontPath;
	public int size;
	
	public SkinID(String fontPath, int size){
		this.fontPath = fontPath;
		this.size = size;
	}
	
	public String toString(){
		return "skin." + fontPath + String.valueOf(size);
	}
}
