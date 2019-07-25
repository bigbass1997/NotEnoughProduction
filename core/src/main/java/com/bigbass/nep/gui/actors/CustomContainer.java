package com.bigbass.nep.gui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CustomContainer<T extends Actor> extends Container<T> {
	
	protected Skin skin;
	
	public CustomContainer(Skin skin){
		super();
		this.skin = skin;
	}
	
	public CustomContainer(T t, Skin skin){
		super(t);
		this.skin = skin;
	}

	public void setBackgroundColor(Color color){
		this.setBackground(skin.newDrawable("whiteBackground", color), false);
	}
}
