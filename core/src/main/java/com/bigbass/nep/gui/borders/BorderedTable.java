package com.bigbass.nep.gui.borders;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.bigbass.nep.gui.NodeManager;
import com.bigbass.nep.util.Singleton;

public class BorderedTable extends Table implements BorderedActor {

	private Drawable border;
	
	public BorderedTable(){
		super();
	}
	
	public BorderedTable(Skin rootSkin) {
		super(rootSkin);
	}

	public <T extends Actor> Cell<T> add(T actor, BorderSide... borders){
		Cell<T> cell = super.add(actor);
		
		if(actor instanceof BorderedActor){
			((BorderedActor) actor).addBorders(borders);
		}
		
		return cell;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		
		if(border != null){
			border.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
	}

	@Override
	public void addBorders(BorderSide[] borders) {
		border = BorderUtil.newDrawable(Singleton.getInstance(NodeManager.class).shapeDrawer, borders);
	}
}
