package com.bigbass.nep.fonts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font {
	
	public BitmapFont font;
	public int size;
	
	public Font(BitmapFont font, int size){
		this.font = font;
		this.size = size;
	}
	
	public String toString(){
		return font.toString() + "-" + String.valueOf(size);
	}
}
