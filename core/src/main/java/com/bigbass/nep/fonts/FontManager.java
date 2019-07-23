package com.bigbass.nep.fonts;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontManager {
	
	private static Hashtable<String, Font> fonts;
	
	/**
	 * @param path Internal file path for font.
	 * @param sizes Can be null. Sizes you want generated for the font.
	 */
	public static void addFont(String path, int[] sizes){
		if(fonts == null) fonts = new Hashtable<String, Font>(); //initialize if first time adding fonts
		if(sizes == null || sizes.length == 0) return; //sizes must not be empty and not null
		
		FileHandle file = Gdx.files.internal(path);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		for(int i = 0; i < sizes.length; i++){
			generator.scaleForPixelHeight(sizes[i]);
			parameter.size = sizes[i];
			parameter.minFilter = Texture.TextureFilter.Nearest;
			parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
			fonts.put(path.concat(String.valueOf(sizes[i])), new Font(generator.generateFont(parameter), sizes[i]));
		}
		generator.dispose();
	}
	
	public static Font getFont(String path, int size){
		Font font = null;
		
		if(fonts != null){
			font = fonts.get(path.concat(String.valueOf(size)));
		}
		
		if(font == null){
			addFont(path, new int[]{size});
			return getFont(path, size);
		}
		
		return font;
	}
}
