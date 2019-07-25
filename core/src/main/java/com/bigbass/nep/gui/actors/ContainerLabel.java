package com.bigbass.nep.gui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigbass.nep.gui.ColorCache;

public class ContainerLabel extends CustomContainer<CustomLabel> {
	
	public CustomLabel label;
	
	public ContainerLabel(Skin skin){
		this(skin, false);
	}
	
	public ContainerLabel(Skin skin, boolean isHoverable){
		super(skin);
		
		label = new CustomLabel(skin);
		setActor(label);
		
		pad(1, 3, 1, 3);
		
		label.setWrap(true);
	}
	
	@Override
	public Container<CustomLabel> minWidth(float minWidth){
		return super.minWidth(minWidth - (this.getPadLeft() + this.getPadRight()));
	}
	
	public void setForegroundColor(Color color){
		this.label.setForegroundColor(ColorCache.getForegroundColor(color));
	}
}