package com.bigbass.nep.gui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CustomLabel extends Label {
	
	public CustomLabel(Skin skin){
		this("", skin);
	}
	
	public CustomLabel(String text, Skin skin) {
		super(text, skin);
	}
	
	public void setForegroundColor(Color color){
		LabelStyle st = new LabelStyle(this.getStyle());
		st.fontColor = color;
		this.setStyle(st);
	}
}
