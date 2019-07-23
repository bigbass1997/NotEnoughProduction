package com.bigbass.nep.skins;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigbass.nep.fonts.FontManager;

public class SkinManager {
	
	private static Hashtable<String, Skin> skins;
	
	public static Skin getSkin(SkinID skinID){
		if(skins == null) skins = new Hashtable<String, Skin>();
		
		if(skins.get(skinID.toString()) == null){
			addSkin(skinID.fontPath, skinID.size);
			return getSkin(skinID);
		}
		
		return skins.get(skinID.toString());
	}

	public static Skin getSkin(String fontPath, int size){
		return getSkin(new SkinID(fontPath, size));
	}
	
	public static void addSkin(String fontPath, int size){
		//TODO Allow specifying the path to the skin files.
		Skin skin = new Skin();
		skin.add("default-font", FontManager.getFont(fontPath, size).font, BitmapFont.class);
		FileHandle fileHandle = Gdx.files.internal("skins/default/skin.json");
		FileHandle atlasFile = Gdx.files.internal("skins/default/skin.atlas");
		skin.addRegions(new TextureAtlas(atlasFile));
		skin.load(fileHandle);
		
		skins.put(new SkinID(fontPath, size).toString(), skin);
	}
}