package com.bigbass.nep.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.bigbass.nep.gui.actors.ContainerLabel;
import com.bigbass.nep.gui.actors.CustomContainer;
import com.bigbass.nep.skins.SkinManager;

public class SearchTableBuilder {

	private static final String FONTPATH = "fonts/droid-sans-mono.ttf";
	
	private SearchTableBuilder(){}
	
	public static void build(Table root, float tableWidth){
		root.reset();
		
		root.setWidth(tableWidth);
		
		textSearchRow(root);
	}
	
	private static void textSearchRow(Table root){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel searchText = new ContainerLabel(SkinManager.getSkin(FONTPATH, 12));
		searchText.label.setText("Search:");
		searchText.setBackgroundColor(Color.CHARTREUSE);
		searchText.setForegroundColor(Color.CHARTREUSE);
		searchText.minWidth(root.getWidth() * 0.5f);
		
		ContainerTextField searchField = new ContainerTextField(SkinManager.getSkin(FONTPATH, 12));
		searchField.minWidth(root.getWidth() * 0.5f);
		
		
		root.add(searchText);
		
		root.add(nested);
	}
	
	
	public static class ContainerTextField extends CustomContainer<TextField> {
		
		public TextField field;
		
		public ContainerTextField(Skin skin) {
			super(skin);
			
			field = new TextField("", skin);
			setActor(field);
			
			pad(1, 3, 1, 3);
		}

		@Override
		public Container<TextField> minWidth(float minWidth){
			return super.minWidth(minWidth - (this.getPadLeft() + this.getPadRight()));
		}
	}
}
