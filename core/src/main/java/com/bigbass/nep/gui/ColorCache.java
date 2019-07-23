package com.bigbass.nep.gui;

import com.badlogic.gdx.graphics.Color;

public class ColorCache {
	
	public static Color getBackgroundColor(String subject){
		return new Color(0.21568f, 0.33725f, 0.13725f, 1);
	}
	
	public static Color getForegroundColor(Color background){
		/*final float lum = (0.2126f * background.r) + (0.7152f * background.g) + (0.0722f * background.b);
		
		if(lum > 0.179){
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}*/
		
		final float lum = (float) (Math.sqrt(0.299f * Math.pow(background.r, 2) + 0.587 * Math.pow(background.g, 2) + 0.114 * Math.pow(background.b, 2)));
		return lum > 0.5f ? Color.BLACK : Color.WHITE;
	}
}
